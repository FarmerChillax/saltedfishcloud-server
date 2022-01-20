package com.xiaotao.saltedfishcloud.service.download;

import com.xiaotao.saltedfishcloud.service.async.context.TaskContextFactory;
import com.xiaotao.saltedfishcloud.service.async.context.TaskManager;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DownloadServiceTest {
    @Resource
    private DownloadService downloadService;
    @Autowired
    private TaskManager taskManager;
    @Resource
    private TaskContextFactory factory;

    @Test
    public void testInterrupt() throws InterruptedException {
        String url = "https://bigota.d.miui.com/V11.0.5.0.PCACNXM/miui_MI6_V11.0.5.0.PCACNXM_996ffd2660_9.0.zip";
        var task = DownloadTaskBuilder.create(url).build();
        var context = factory.createContextFromAsyncTask(task);
        taskManager.submit(context);
        downloadService.interrupt(context.getId());
        Thread.sleep(500);
        assertTrue(task.isInterrupted());
    }
}
