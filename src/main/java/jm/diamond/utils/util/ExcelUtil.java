package jm.diamond.utils.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.FileCopyUtils;

@Slf4j
public class ExcelUtil {

   private static final String FILE_PATH = System.getProperty("user.dir") + File.separator + "excelTemp" + File.separator;


   /**
    * 엑셀 파일 생성
    * @param fileName 생성할 파일 이름
    * @param sheetName 생성할 시트 이름
    * @return String 엑셀파일 경로
    */
   public static String createExcel(String fileName, String sheetName
       , List<HashMap<String, String>> headerList
       , List<HashMap<String, String>>	dataList
   ){

      try(SXSSFWorkbook wb = new SXSSFWorkbook(500);
      ){

         createDirectory(FILE_PATH);
         cleanOldFiles(FILE_PATH);

         // 1. create sheet
         wb.createSheet(sheetName);
         log.info("Excel file created: {}/{}", FILE_PATH, fileName);

         // 2. insert data
         insertData(wb,headerList,dataList);

         //3. create temp excel
         String excelPath = createTempExcel(wb, fileName);

         return excelPath;

      }catch (Exception e){
         e.printStackTrace();
         throw new RuntimeException();
      }

   }

   /**
    * 엑셀 파일 다운로드
    * @param response HTTP 응답 객체
    * @param filePath 다운로드할 파일 경로
    */
   public static void downloadExcelFile(HttpServletResponse response, String filePath) {
      validateFilePath(filePath);
      File file = new File(FILE_PATH );

      if (!file.exists()) {
         log.warn("File not found: {}", filePath);
         response.setStatus(HttpServletResponse.SC_NOT_FOUND);
         try {
            response.getWriter().write("File not found: " + filePath);
         } catch (IOException e) {
            log.error("Error writing error response", e);
         }
         return;
      }

      try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
         String	mimeType	=	"application/x-msdownload";
         response.setContentType(mimeType);
         response.setContentLength((int) file.length());
         response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

         FileCopyUtils.copy(in, response.getOutputStream());
         response.flushBuffer();
         log.info("Excel file downloaded: {}", filePath);
      } catch (IOException e) {
         log.error("Error downloading Excel file", e);
         response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
         try {
            response.getWriter().write("Failed to download file");
         } catch (IOException ioException) {
            log.error("Error writing error response", ioException);
         }
      }
   }



   /**
    * 엑셀 파일 생성
    * @param fileName 생성할 파일 이름
    * @param sheetName 생성할 시트 이름
    * @return SXSSFWorkbook 엑셀 객체
    */
   private static SXSSFWorkbook createWorkBook(String fileName, String sheetName) {
      SXSSFWorkbook wb;

      try {
         String sanitizedFileName = sanitizeFileName(fileName);
         createDirectory(FILE_PATH);
         cleanOldFiles(FILE_PATH);

         wb = new SXSSFWorkbook(500);
         wb.createSheet(sheetName);

         log.info("Excel file created: {}/{}", FILE_PATH, sanitizedFileName);
         return wb;
      } catch (Exception e) {
         log.error("Error creating Excel file", e);
         throw new RuntimeException("Failed to create Excel file", e);
      } finally {

      }
   }

   /**
    * 데이터를 엑셀에 삽입
    * @param workbook 엑셀 워크북 객체
    * @param headers 엑셀 헤더 정보
    * @param data 데이터 리스트
    */
   public static void insertData(SXSSFWorkbook workbook, List<HashMap<String, String>> headers, List<HashMap<String, String>> data) {
      try {
         Sheet sheet = workbook.getSheetAt(0);

         // 컬럼 헤더 작성
         Row headerRow = sheet.createRow(0);
         for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i).get("HEADER_NM"));
            sheet.setColumnWidth(i, 4000);
         }

         // 데이터 삽입
         for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < headers.size(); j++) {
               String colName = headers.get(j).get("COL_NM");
               String value = data.get(i).get(colName);
               row.createCell(j).setCellValue(value == null ? "" : value);
            }

            // 주기적으로 메모리 정리
            if (i % 500 == 0) {
               ((SXSSFSheet) sheet).flushRows(500); // 500개 행을 디스크로 플러시
               log.info("Flushed rows up to row {}", i);
            }
         }


      } catch (Exception e) {
         log.error("Error inserting data into Excel", e);
         throw new RuntimeException("Failed to insert data into Excel", e);
      }
   }

   /**
    * 엑셀 파일 저장
    * @param workbook SXSSFWorkbook 객체
    * @param fileName 저장할 파일 이름
    * @return 저장된 파일 경로
    */
   public static String saveExcelFile(SXSSFWorkbook workbook, String fileName) {
      String sanitizedFileName = sanitizeFileName(fileName);
      String filePath = FILE_PATH + sanitizedFileName;

      try (FileOutputStream out = new FileOutputStream(filePath)) {
         workbook.write(out);
         log.info("Excel file saved: {}", filePath);
         return filePath;
      } catch (IOException e) {
         log.error("Error saving Excel file", e);
         throw new RuntimeException("Failed to save Excel file", e);
      } finally {
         if (workbook != null) {
            workbook.dispose(); // 항상 dispose 호출
         }
      }
   }

   public static String createTempExcel(SXSSFWorkbook workbook, String fileName) {
      String sanitizedFileName = sanitizeFileName(fileName);
      String filePath = FILE_PATH + sanitizedFileName;

      try (FileOutputStream out = new FileOutputStream(filePath)) {
         workbook.write(out);
         log.info("Excel file saved: {}", filePath);
         return filePath;
      } catch (IOException e) {
         log.error("Error saving Excel file", e);
         throw new RuntimeException("Failed to save Excel file", e);
      } finally {
         if (workbook != null) {
            workbook.dispose(); // 항상 dispose 호출
         }
      }
   }




   /**
    * 파일 이름을 안전하게 변환
    */
   private static String sanitizeFileName(String fileName) {
      if (fileName == null || fileName.trim().isEmpty()) {
         fileName = "ExcelFile";
      }
      String curDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      return fileName.replaceAll("[\\/:*?\"<>|]", "_") + "_" + curDate + ".xlsx";
   }

   /**
    * 디렉토리 생성
    */
   private static void createDirectory(String path) {
      File dir = new File(path);
      if (!dir.exists()) {
         if (dir.mkdirs()) {
            log.info("Directory created: {}", path);
         } else {
            log.warn("Failed to create directory: {}", path);
         }
      }
   }

   /**
    * 오래된 파일 정리
    */
   private static void cleanOldFiles(String path) {
      File dir = new File(path);
      File[] files = dir.listFiles();

      if (files != null) {
         long threshold = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1); // 1일 이전 파일 삭제
         for (File file : files) {
            if (file.getName().endsWith(".xlsx") && file.lastModified() < threshold) {
               if (file.delete()) {
                  log.info("Deleted old file: {}", file.getName());
               } else {
                  log.warn("Failed to delete old file: {}", file.getName());
               }
            }
         }
      }
   }

   /**
    * 파일 경로 유효성 검사
    */
   private static void validateFilePath(String filePath) {
      File file = new File(filePath);
      File baseDir = new File(FILE_PATH);
      try {
         if (!file.getCanonicalPath().startsWith(baseDir.getCanonicalPath())) {
            throw new SecurityException("Invalid file path: " + filePath);
         }
      } catch (IOException e) {
         throw new RuntimeException("Failed to validate file path", e);
      }
   }

}
