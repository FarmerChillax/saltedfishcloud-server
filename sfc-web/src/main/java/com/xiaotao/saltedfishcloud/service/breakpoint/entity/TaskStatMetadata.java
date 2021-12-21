package com.xiaotao.saltedfishcloud.service.breakpoint.entity;

import com.xiaotao.saltedfishcloud.service.breakpoint.exception.TaskNotFoundException;
import com.xiaotao.saltedfishcloud.service.breakpoint.manager.impl.utils.TaskStorePath;
import com.xiaotao.saltedfishcloud.service.breakpoint.merge.MergeInputStream;
import com.xiaotao.saltedfishcloud.service.breakpoint.merge.MultipleFileMergeInputStreamGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务状态数据，除了元数据本身，还附加了任务的当前完成状态
 */
@Slf4j
public class TaskStatMetadata extends TaskMetadata {

    private List<Integer> finishPart;

    /**
     * @param data 基础的任务元数据
     * @throws TaskNotFoundException 任务ID不存在
     */
    public TaskStatMetadata(TaskMetadata data) {
        super(data.getTaskId(), data.getFileName(), data.getLength());
        if (!Files.exists(TaskStorePath.getRoot(data.getTaskId()))) {
            throw new TaskNotFoundException(data.getTaskId());
        }
    }


    /**
     * 更新任务的完成状态信息。
     * @TODO 通过文件大小进行数据校验
     */
    public void fresh() throws IOException {
        finishPart = Files.list(TaskStorePath.getRoot(getTaskId()))
                    .filter(e -> e.toString().endsWith(".part"))
                    .map(e -> Integer.parseInt(e.getFileName().toString().replaceAll(".part", "")))
                    .sorted()
                    .collect(Collectors.toList());
    }

    public List<Integer> getFinishPart() throws IOException {
        if (finishPart == null) {
            fresh();
        }
        return finishPart;
    }

    /**
     * 返回该分块任务是否已完成
     */
    public boolean isFinish() {
        try {
            return getFinishPart().size() == getChunkCount();
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public MergeInputStream getMergeInputStream() throws IOException {
        if (!this.isFinish()) {
            throw new IllegalStateException("断点续传任务未完成,文件块不完整");
        }
        int len = getChunkCount();
        Path[] paths = new Path[len];
        for (Integer integer : finishPart) {
            paths[integer - 1] = TaskStorePath.getPartFile(taskId, integer);
        }
        return new MergeInputStream(new MultipleFileMergeInputStreamGenerator(paths));
    }

}
