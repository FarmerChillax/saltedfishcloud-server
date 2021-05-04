package com.xiaotao.saltedfishcloud.utils;

import com.xiaotao.saltedfishcloud.exception.HasResultException;
import com.xiaotao.saltedfishcloud.po.file.DirCollection;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;

@Slf4j
public class FileUtils {
    private static final HashMap<String,String> map = new HashMap<>();
    static {
        // 一般网页资源
        map.put("html", "text/html;charset=utf-8");
        map.put("htm", "text/html;charset=utf-8");
        map.put("js", "application/x-javascript;charset=utf-8");
        map.put("css", "text/css;charset=utf-8");
        map.put("txt", "text/plain;charset=utf-8");

        // 图片
        map.put("gif", "image/gif");
        map.put("jpg", "image/jpeg");
        map.put("jpeg", "image/jpeg");
        map.put("png", "image/png");
        map.put("ico", "image/x-icon");

        // 音乐
        map.put("mp3", "audio/mp3");
        map.put("mp2", "audio/mp2");
        map.put("ogg", "audio/ogg");
        map.put("ape", "audio/ape");
        map.put("flac", "audio/flac");

        // 视频
        map.put("mp4", "video/mp4");

        // 视频
        map.put("pdf", "application/pdf");
    }

    /**
     * 通过文件名获取对应文件类型的Content-Type
     * @param name  文件名
     * @return Content-Type结果
     */
    static public String getContentType(String name) {
        name = getSuffix(name);
        String res = map.get(name);
        return res == null ? "application/octet-stream" : res;
    }

    /**
     * 搜索遍历目录，取出文件夹下的所有文件和目录
     * @param path 本地文件夹路径
     * @return DirCollection对象
     */
    static public DirCollection scanDir(String path) throws IOException {
        DirCollection res = new DirCollection();
        Path root = Paths.get(path);
        Files.walkFileTree(root , new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                res.addFile(file.toFile());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!Files.isSameFile(dir, root)) {
                    res.addFile(dir.toFile());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return res;
    }


    /**
     * 获取文件名中的后缀名，即最后一个字符'.'后面的子字符串，若无后缀名，则返回整个原文件名
     * @author xiaotao
     * @param name  文件名或文件路径
     * @return  后缀名，不带'.'
     */
    public static String getSuffix(String name) {
        String[] split = name.split("\\.");
        if (split.length != 0) {
            return split[split.length - 1].toLowerCase();
        } else {
            return name;
        }
    }


    /**
     * 通过输入流保存一个文件，若outputFile是文件夹 则异常
     * 传入的InputStream将在文件写入完毕后被关闭
     * @TODO 改用Java nio进行重构并逐步使用java.nio取代java.io
     * @param inputStream   文件的输入流
     * @param outputFile    保存到的位置
     * @return 若发生覆盖返回1 否则返回0
     * @throws HasResultException 出错
     */
    public static int writeFile(InputStream inputStream, File outputFile) throws HasResultException {
        int res = outputFile.exists() ? 1 : 0;
        if (res == 1 && outputFile.isDirectory()) throw new HasResultException("已存在同名文件夹");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[8192];
            int len = 0;
            while ( (len = inputStream.read(buffer)) != -1 ) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new HasResultException(400, e.getMessage());
        }
        return res;
    }

    /**
     * 合并两个本地目录的内容（包括子目录和文件，文件同名将被覆盖）
     * @param source        源目录，合并完成后将被删除
     * @param target        被合并到的位置
     * @param overwrite     是否覆盖已有文件（若为false，源文件和目录将仍被删除）
     */
    public static void mergeDir(String source, String target, boolean overwrite) throws IOException {
        DirCollection sourceCollection = scanDir(source);
        if (!Files.exists(Paths.get(target))) {
            throw new NoSuchFileException(target);
        }

        //  检查子目录和文件同名情况下类型是否一致
        Consumer<File> consumer = file -> {
            Path p = Paths.get(target + "/" + StringUtils.removePrefix(source, file.getPath()));
            if (Files.exists(p)) {
                if (file.isDirectory() != Files.isDirectory(p)) {
                    throw new UnsupportedOperationException("已存在文件与被移动文件类型不一致: " + file.getName());
                }
            }
        };
        sourceCollection.getFileList().forEach(consumer);
        sourceCollection.getDirList().forEach(consumer);

        for (File file : sourceCollection.getDirList()) {
            Path p = Paths.get(target + "/" + StringUtils.removePrefix(source, file.getPath()));
            if (!Files.exists(p)) Files.createDirectory(p);
        }

        for (File file : sourceCollection.getFileList()) {
            Path p = Paths.get(target + "/" + StringUtils.removePrefix(source, file.getPath()));
            if (overwrite) Files.move(Paths.get(file.getPath()), p, StandardCopyOption.REPLACE_EXISTING);
            else file.delete();
            log.debug("move " + file.getPath() + " -> " + p);
        }

        //  删除源文件夹
        Collections.reverse(sourceCollection.getDirList());
        sourceCollection.getDirList().forEach(File::delete);
        Files.delete(Paths.get(source));
    }
}
