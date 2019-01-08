import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        Map<String, String> map = new HashMap<String, String>();

        try {

            DiskFileItemFactory factor = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(factor);
            List<FileItem> list = fileUpload.parseRequest(request);

            for (FileItem item : list) {

                if (item.isFormField()) {

                    map.put(item.getFieldName(), item.getString("utf-8"));

                } else {

                    InputStream is = item.getInputStream();

                    String realPath = getServletContext().getRealPath("/images");
                    File file = new File(realPath,UUIDUtils.getId() + item.getName());

                    if (!file.exists()){
                        file.createNewFile();
                    }

                    OutputStream os = new FileOutputStream(file);

                    IOUtils.copy(is,os);
                    is.close();
                    os.close();

                    map.put("userhead","/images/" + UUIDUtils.getId() + item.getName());
                }

            }

             System.out.println(map);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}






















