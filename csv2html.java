import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileWriter;

/**
 * author : 金志昊
 */

class csv2html {

    public static String readFile(String strFile){
        try {
            Logger log = Logger.getLogger("lavasoft");
            log.setLevel(Level.INFO);
            InputStream is = new FileInputStream(strFile);
            int iAvail = is.available();
            byte[] bytes = new byte[iAvail];
            is.read(bytes);
            String csv = new String(bytes);
            log.info("文件内容:\n"+ csv);
            is.close();
            return csv;
        }catch(Exception e){
            e.printStackTrace();
            return new String("");
        }
    }

    public static void saveFile(String strFile,String FileName){
        try {
            File file = new File(FileName);
            Writer out = new FileWriter(file);
            out.write(strFile);
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String escapeChars(String lineIn){
        StringBuilder sb = new StringBuilder();
        int lineLength = lineIn.length();

        for(int i = 0; i < lineLength; i++){
            char c = lineIn.charAt(i);
            switch(c){
                case '"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '<' :
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(c);
            }
        }

        return sb.toString();
    }

    public static void buildRow(StringBuilder sb, String[] columns){
        sb.append("<trv>");
        for(String element: columns){
            sb.append("<th>");
            sb.append(escapeChars(element));
            sb.append("</th>");
        }
        sb.append("</tr>\n");
    }

    public static void main(String[] args) throws Exception {
        //读取文件
        String csv = readFile(args[0]);
        String[] lines = csv.split("\n");
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        sb.append("<head><meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\"/>\n");
        sb.append(new String("<title>CSV2HTML:" + args[0] + "</title>\n"));
        sb.append("<style type=\"text/css\">");
        sb.append("body{background-color:#FFF;color:#000;font-family:OpenSans,sans-serif;font-size:10px;}");
        sb.append("table{border:0.2em solid #2F6FAB;border-collapse:collapse;}");
        sb.append("th{border:0.15em solid #2F6FAB;padding:0.5em;background-color:#E9E9E9;}");
        sb.append("td{border:0.1em solid #2F6FAB;padding:0.5em;background-color:#F9F9F9;}</style>");
        sb.append("</head><body><h1>CSV2HTML:" + args[0] + "</h1>\n");
        sb.append("<table>\n");

        for (String line : lines) {
            String[] columns = line.split(",");
            buildRow(sb, columns);
        }
            
        sb.append("</table></body></html>");

        //以html的形式表存在文件所在文件夹
        String[] names = args[0].split("\\.");
        String FileName = new String(names[0] + ".html");
        saveFile(sb.toString(), FileName);
    }
}