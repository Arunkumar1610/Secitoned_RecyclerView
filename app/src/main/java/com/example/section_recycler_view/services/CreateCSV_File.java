package com.example.section_recycler_view.services;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.section_recycler_view.dataSet.SectionHeader;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class CreateCSV_File {

    Context context;

    List<SectionHeader> sectionHeader;

    public CreateCSV_File(Context context, List<SectionHeader> sectionHeader) {
        this.context = context;
        this.sectionHeader = sectionHeader;
    }


//    long imagename = System.currentTimeMillis();
    int c = 0;
    public void generateCSV() {
        String fileName =String.valueOf(System.currentTimeMillis());
        // File filePath = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS);
        File filePath = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + fileName + ".xls");

        try {

            if (!filePath.exists()) {
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                HSSFSheet hssfSheet = hssfWorkbook.createSheet("MySheet");
                //Row 1
                HSSFRow row1 = ((HSSFSheet) hssfSheet).createRow(0);
                HSSFCell cell1 = row1.createCell(0);
                cell1.setCellValue("TransactionID");
                HSSFCell cell2 = row1.createCell(1);
                cell2.setCellValue("Date");
                HSSFCell cell3 = row1.createCell(2);
                cell3.setCellValue("Amount");

                int count = 1;

                for (int i = 0; i < sectionHeader.size(); i++) {

                    for (int j = 0; j < sectionHeader.get(i).getChildItems().size(); j++) {

                        HSSFRow hssfRow1 = ((HSSFSheet) hssfSheet).createRow(count);

                        HSSFCell hssfCell1 = hssfRow1.createCell(0);
                        hssfCell1.setCellValue(sectionHeader.get(i).getChildItems().get(j).getTxn_id());

                        HSSFCell hssfCell2 = hssfRow1.createCell(1);
                        hssfCell2.setCellValue(sectionHeader.get(i).getChildItems().get(j).getDate());

                        HSSFCell hssfCell3 = hssfRow1.createCell(2);
                        hssfCell3.setCellValue(sectionHeader.get(i).getChildItems().get(j).getAmount());

                        count++;
                    }
                }

                filePath.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                hssfWorkbook.write(fileOutputStream);

                Toast.makeText(context, "File Created", Toast.LENGTH_SHORT).show();

                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } else {

               fileName="";
               fileName =String.valueOf(System.currentTimeMillis());
                generateCSV();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
