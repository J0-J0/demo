package com.jojo.controller.debug;

import com.jojo.persistent.model.SysTask;
import com.jojo.pojo.Response;
import com.jojo.service.SysTaskService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    private SysTaskService sysTaskService;

    @RequestMapping("/mongo")
    public Response testMongo() throws FileNotFoundException {
        Response response = new Response();

        // 增
//        String filepath = "C:\\Users\\72669\\Desktop\\QQ图片20190801175241.png";
//        File file = new File(filepath);
//        String fileName = SnowFlakerUtil.getSnowflakeId() + "";
//        ObjectId objectId = gridFsTemplate.store(new FileInputStream(file), fileName);

        // 删
//        Criteria criteria = GridFsCriteria.whereFilename().is("607532119056449536");
//        Query query = Query.query(criteria);
//        gridFsTemplate.delete(query);

//        response.setData(gridFSFile.getObjectId());
        response.setSuccessMessage("执行完毕");
        return response;
    }

    @RequestMapping("/selectAndShowFile")
    public void testSelectAndShowFile(@RequestParam String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Criteria criteria = GridFsCriteria.whereFilename().is(filename);
        Query query = Query.query(criteria);
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        GridFsResource gridFsResource = gridFsTemplate.getResource(gridFSFile);
        InputStream inputStream = gridFsResource.getInputStream();

        response.setHeader("content-type", "image/jpeg");
        ServletOutputStream outputStream = response.getOutputStream();

        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();

        try {
        } finally {
            IOUtils.closeQuietly(inputStream, outputStream);
        }
    }

    @RequestMapping("/dubbo")
    public Response dubboRun() {
        Response response = new Response();
        List<SysTask> sysTaskList = sysTaskService.selectAll();
        response.setData(sysTaskList);
        return response;
    }
}
