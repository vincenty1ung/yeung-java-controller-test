## Excel-Poi工具类库<br>
首先当然是要添加poi的maven依赖了:<br>
```xml
		<!-- https://mvnrepository.com/artifact/org.apache.poi/poi -->
		<dependency>
		    <groupId>org.apache.poi</groupId>
		    <artifactId>poi</artifactId>
		    <version>3.9</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
		<dependency>
		    <groupId>org.apache.poi</groupId>
		    <artifactId>poi-ooxml</artifactId>
		    <version>3.9</version>
		</dependency>	
```
* 支持大数据量的Excel-Poi工具类:<br>
```java
/** 
 * ExcelUtil: 采用的是POI3.9的SXSSFWorkbook  excel版本在2007以上
 * 支持大数据量！！！！！！！！！！几十几百万不是问题！！！！！！！
 * @version 1.0
 * @author 15989
 * @modified 2016-10-20 v1.0 15989 新建 
 */
public class ExcelUtil<T> implements Serializable {

    private static final Log logger = LogFactory.getLog(ExcelUtil.class);
    /** 一个sheet最大的数据量 */
    public final static int sheetSize = Integer.parseInt(ConfigService.getValue("EXCELCONFIG", "sheetMaxVal"));
    /** 内存一次缓存最大数据量,满了之后清空,重新换一批数据 */
    public final static int diskSize = Integer.parseInt(ConfigService.getValue("EXCELCONFIG", "cacheMaxVal"));

    /**
     * 获取相应的类
     */
    private Class<T> clazz;
    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     *
     * @param resultList 将写入EXCEL的数据
     * @param sheetName 工作表名字
     * @param outputStream 输出流
     * @return
     */
    public boolean writeExcelFromList(List<T> resultList, String sheetName, OutputStream outputStream){
        //返回标示
        Boolean sign = Boolean.TRUE;
        try{
            // 得到所有定义字段
            Field[] allFields = clazz.getDeclaredFields();
            List<Field> fields = new ArrayList<Field>();
            // 得到所有field并存放到一个list中
            for (Field field : allFields) {
                if (field.isAnnotationPresent(ExcelAttribute.class)) {
                    fields.add(field);
                }
            }
            // 产生工作薄对象
            Workbook workbook = new SXSSFWorkbook(diskSize);

            //数据源数量
            int listSize = 0;
            if (resultList != null && resultList.size() >= 0) {
                listSize = resultList.size();
            }
            //工作簿页数
            double sheetNo = Math.ceil(listSize / sheetSize);      
            boolean addSheetFlag = (listSize%sheetSize)>0?true:false;

            for(int i = 0 ; i <= sheetNo ; i++){
            	if (((sheetNo!=0) && (i!=sheetNo)) || ((sheetNo==0) && (sheetNo==i)) || ((sheetNo!=0) && addSheetFlag)) {
            		 //创建工作簿
                    Sheet sheet = workbook.createSheet();
                    //设置工作表的名称
                    workbook.setSheetName(i,sheetName+""+i);
                    //创建
                    Row row;
                    Cell cell;
                    //创建第一行
                    row = sheet.createRow(0);
                    for(int cellNum = 0 ; cellNum < fields.size() ; cellNum++){
                        //
                        Field field = fields.get(cellNum);
                        //获取注解信息
                        ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                        int col = cellNum;
                        // 根据指定的顺序获得列号
                        if (StringUtils.isNotBlank(attr.column())) {
                            col = getExcelCol(attr.column());
                        }
                        // 创建列
                        cell = row.createCell(col);

                        sheet.setColumnWidth(i, (int) ((attr.name().getBytes().length <= 4 ? 6 : attr.name().getBytes().length) * 1.5 * 256));

                        // 设置列中写入内容为String类型
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        // 写入列名
                        cell.setCellValue(attr.name());
                        /*
                        // 如果设置了提示信息则鼠标放上去提示.
                        if (StringUtils.isNotBlank(attr.prompt())) {
                            setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);
                        }
                        // 如果设置了combo属性则本列只能选择不能输入
                        if (attr.combo().length > 0) {
                            setHSSFValidation(sheet, attr.combo(), 1, 100, col, col);
                        }
                        */
                    }

                    //创建内容列
                    int startNo = i * sheetSize;
                    int endNo = Math.min(startNo + sheetSize, listSize);
                    for(int j = startNo; j < endNo; j++){
                        row = sheet.createRow(j + 1 - startNo);
                        // 得到导出对象.
                        T vo = (T) resultList.get(j);
                        for(int k = 0 ; k < fields.size() ; k++){
                            // 获得field
                            Field field = fields.get(k);
                            // 设置实体类私有属性可访问
                            field.setAccessible(true);
                            ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                            int col = k;
                            // 根据指定的顺序获得列号
                            if (StringUtils.isNotBlank(attr.column())) {
                                col = getExcelCol(attr.column());
                            }

                            cell = row.createCell(col);
                            // 如果数据存在就填入,不存在填入空格
                            Class<?> classType = (Class<?>) field.getType();
                            String value = null;
                            if (field.get(vo) != null && classType.isAssignableFrom(Date.class)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                                
                                value = sdf.format(sdf.parse(String.valueOf(field.get(vo))));
                            }
                            cell.setCellValue(field.get(vo) == null ? "" : value == null ? String.valueOf(field.get(vo)) : value);

                        }
                    }
				}
            }
            outputStream.flush();
            workbook.write(outputStream);            
        }catch (Exception e){
            logger.warn("Excel writeExcelFromList Exception" + e);
        }finally {
        	try {
				outputStream.close();
			} catch (IOException e) {
				logger.error("关闭流出错：" + e);
			}
            return sign;
        }
    }


    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     *
     * @param col
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

}
```
* 支持少数据量的数据导入到excel工具类:<br>
```java
public class ExportExcel {
    
  //显示的导出表的标题  
  private String title;  
  //导出表的列名  
  private String[] rowName ;  
    
  private List<Object[]>  dataList = new ArrayList<Object[]>();  
    
  HttpServletResponse  response;  
    
  //构造方法，传入要导出的数据  
  public ExportExcel(String title,String[] rowName,List<Object[]>  dataList){  
      this.dataList = dataList;  
      this.rowName = rowName;  
      this.title = title;  
  }  
            
  /* 
   * 导出数据 
   * */  
  public void export(HttpServletResponse httpServletResponse) throws Exception{  
      try{  
          HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象  
          HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表  
            
          // 产生表格标题行  
          HSSFRow rowm = sheet.createRow(0);  
          HSSFCell cellTiltle = rowm.createCell(0);  
            
          //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
          HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象  
          HSSFCellStyle style = this.getStyle(workbook);                  //单元格样式对象  
            
          sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length-1)));    
          cellTiltle.setCellStyle(columnTopStyle);  
          cellTiltle.setCellValue(title);  
            
          // 定义所需列数  
          int columnNum = rowName.length;  
          HSSFRow rowRowName = sheet.createRow(2);                // 在索引2的位置创建行(最顶端的行开始的第二行)  
            
          // 将列头设置到sheet的单元格中  
          for(int n=0;n<columnNum;n++){  
              HSSFCell  cellRowName = rowRowName.createCell(n);               //创建列头对应个数的单元格  
              cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型  
              HSSFRichTextString text = new HSSFRichTextString(rowName[n]);  
              cellRowName.setCellValue(text);                                 //设置列头单元格的值  
              cellRowName.setCellStyle(columnTopStyle);                       //设置列头单元格样式  
          }  
            
          //将查询出的数据设置到sheet对应的单元格中  
          for(int i=0;i<dataList.size();i++){  
              Object[] obj = dataList.get(i);//遍历每个对象  
              HSSFRow row = sheet.createRow(i+3);//创建所需的行数  
              for(int j=0; j<obj.length; j++){  
                  HSSFCell  cell = null;   //设置单元格的数据类型  
                  if(j == 0){  
                      cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);  
                      cell.setCellValue(i+1);   
                  }else{  
                      cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);  
                      if(!"".equals(obj[j]) && obj[j] != null){  
                          cell.setCellValue(obj[j].toString());                       //设置单元格的值  
                      }  
                  }  
                  cell.setCellStyle(style);                                   //设置单元格样式  
              }  
          }  
          //让列宽随着导出的列长自动适应  
          for (int colNum = 0; colNum < columnNum; colNum++) {  
              int columnWidth = sheet.getColumnWidth(colNum) / 256;  
              for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {  
                  HSSFRow currentRow;  
                  //当前行未被使用过  
                  if (sheet.getRow(rowNum) == null) {  
                      currentRow = sheet.createRow(rowNum);  
                  } else {  
                      currentRow = sheet.getRow(rowNum);  
                  }  
                  if (currentRow.getCell(colNum) != null) {  
                      HSSFCell currentCell = currentRow.getCell(colNum);  
                      if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {  
                          int length = currentCell.getStringCellValue().getBytes().length;  
                          if (columnWidth < length) {  
                              columnWidth = length;  
                          }  
                      }  
                  }  
              }  
              if(colNum == 0){  
                  sheet.setColumnWidth(colNum, (columnWidth-2) * 256);  
              }else{  
                  sheet.setColumnWidth(colNum, (columnWidth+4) * 256);  
              }  
          }  
            
 /*         if(workbook !=null){  
              try  
              {  
                  String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";  
                  String headStr = "attachment; filename=\"" + fileName + "\"";  
                  response = httpServletResponse;  
                  response.setContentType("APPLICATION/OCTET-STREAM");  
                  response.setHeader("Content-Disposition", headStr);  
                  OutputStream out = response.getOutputStream();  
                  workbook.write(out);  
              }  
              catch (IOException e)  
              {  
                  e.printStackTrace();  
              }  
          }  */
          workbook.write(httpServletResponse.getOutputStream());

      }catch(Exception e){  
          e.printStackTrace();  
      }  
        
  }  
    
  /*  
   * 列头单元格样式 
   */      
  public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {  
        
        // 设置字体  
        HSSFFont font = workbook.createFont();  
        //设置字体大小  
        font.setFontHeightInPoints((short)11);  
        //字体加粗  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        //设置字体名字   
        font.setFontName("Courier New");  
        //设置样式;   
        HSSFCellStyle style = workbook.createCellStyle();  
        //设置底边框;   
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        //设置底边框颜色;    
        style.setBottomBorderColor(HSSFColor.BLACK.index);  
        //设置左边框;     
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        //设置左边框颜色;   
        style.setLeftBorderColor(HSSFColor.BLACK.index);  
        //设置右边框;   
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        //设置右边框颜色;   
        style.setRightBorderColor(HSSFColor.BLACK.index);  
        //设置顶边框;   
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        //设置顶边框颜色;    
        style.setTopBorderColor(HSSFColor.BLACK.index);  
        //在样式用应用设置的字体;    
        style.setFont(font);  
        //设置自动换行;   
        style.setWrapText(false);  
        //设置水平对齐的样式为居中对齐;    
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        //设置垂直对齐的样式为居中对齐;   
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
          
        return style;  
          
  }  
    
  /*   
   * 列数据信息单元格样式 
   */    
  public HSSFCellStyle getStyle(HSSFWorkbook workbook) {  
        // 设置字体  
        HSSFFont font = workbook.createFont();  
        //设置字体大小  
        //font.setFontHeightInPoints((short)10);  
        //字体加粗  
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        //设置字体名字   
        font.setFontName("Courier New");  
        //设置样式;   
        HSSFCellStyle style = workbook.createCellStyle();  
        //设置底边框;   
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        //设置底边框颜色;    
        style.setBottomBorderColor(HSSFColor.BLACK.index);  
        //设置左边框;     
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        //设置左边框颜色;   
        style.setLeftBorderColor(HSSFColor.BLACK.index);  
        //设置右边框;   
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        //设置右边框颜色;   
        style.setRightBorderColor(HSSFColor.BLACK.index);  
        //设置顶边框;   
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        //设置顶边框颜色;    
        style.setTopBorderColor(HSSFColor.BLACK.index);  
        //在样式用应用设置的字体;    
        style.setFont(font);  
        //设置自动换行;   
        style.setWrapText(false);  
        //设置水平对齐的样式为居中对齐;    
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        //设置垂直对齐的样式为居中对齐;   
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
         
        return style;  
    
  }  
}  

```
那么，有了工具类之后，如何在业务内操作使用呢，如下代码块:<br>
```java
	public void getExportStaffs(Map<String, Object> queryParams,HttpServletResponse response){
		ExcelUtil<Staff> excelUtil = new ExcelUtil<Staff>(Staff.class);
		List<Staff> oTempList ;
		List<Staff> oList = new ArrayList<Staff>();
		final int fetchSize = 1000;
		try {
			Integer total = this.getCountOfStaff(queryParams);
			int loopSize = (total / fetchSize) + 1;
			for(int i=0; i<loopSize; i++){
				oTempList = new ArrayList<Staff>();
				oTempList = getExportStaffs(queryParams,i,fetchSize);
				oList.addAll(oTempList);
			}
			String fileName = "staffs.xlsx";  
	        response.setHeader("Content-disposition", "attachment; filename="+
			new String(fileName.getBytes("utf-8"), "ISO8859-1"));  
	        response.setContentType("application/octet-stream");  
			excelUtil.writeExcelFromList(oList, "staffs", response.getOutputStream());
		} catch (Exception e) {
			log.error("导出人员信息表失败>>:"+e);
		}
	}

	public List<Staff> getExportStaffs(Map<String, Object> queryParams, int page,
			int fetchSize) {
		return staffDao.getExportStaffs(queryParams,page,fetchSize);
	}
```
