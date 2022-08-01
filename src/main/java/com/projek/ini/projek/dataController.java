/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.projek.ini.projek;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nafi
 */
@Controller
public class dataController {

    DataJpaController dataController = new DataJpaController();

    @RequestMapping("/read")
    public String getData(Model m) {
        List<Data> data = new ArrayList<>();
        int record = dataController.getDataCount();
        String result = "";
        try {
            data = dataController.findDataEntities().subList(0, record);
        } catch (Exception e) {
            result = e.getMessage();
        }
        m.addAttribute("Data", data);
        m.addAttribute("record", record);
        return "view";
    }

    @RequestMapping("/create")
    public String createData() {
        return "create";
    }

    @PostMapping(value = "/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String newDummy(@RequestParam("gambar") MultipartFile file, HttpServletRequest request) throws ParseException, Exception {

        Data dumData = new Data();

        int id = Integer.parseInt(request.getParameter("id"));

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("tanggal"));

        byte[] image = file.getBytes();

        dumData.setId(id);
        dumData.setTanggal(date);
        dumData.setGambar(image);

        dataController.create(dumData);

        return "redirect:/read";
    }


    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })

    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
        Data data = dataController.findData(id);
        byte[] img = data.getGambar();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
    }

    @GetMapping("/delete/{id}")
    public String deleteData(@PathVariable("id") int id) throws Exception {
        dataController.destroy(id);
        
        return "redirect:/read";
    }

    @RequestMapping("/edit/{id}")
    public String updateDATA(@PathVariable("id") int id, Model model) throws Exception {
        Data data = dataController.findData(id);
        model.addAttribute("data", data);
        return "update";
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateData(@RequestParam("gambar") MultipartFile f, HttpServletRequest r) throws ParseException, Exception {
        Data data = new Data();

        int id = Integer.parseInt(r.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tanggal"));
        byte[] image = f.getBytes();

        data.setId(id);
        data.setTanggal(date);
        data.setGambar(image);

        dataController.edit(data);
        return "redirect:/read";
    }
}
