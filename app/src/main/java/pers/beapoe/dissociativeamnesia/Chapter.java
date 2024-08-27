package pers.beapoe.dissociativeamnesia;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Process;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class Chapter implements Serializable {
    // 定义章节名称
    private String name;
    // 定义是否已读
    private boolean isRead = false;
    // 定义章节名称
    private String ChapterName;
    // 定义特殊标识符
    private boolean isSpecial = false;
    // 定义样式组
    private ArrayList<Span> spans = new ArrayList<>();
    // 定义Spannable内容
    private SpannableStringBuilder content = new SpannableStringBuilder();
    // 定义String内容
    private String result = "";

    // 构造函数
    public Chapter(String name,boolean isSpecial,Context context){
        final String TAG = "Chapter:Chapter(...)";
        if(!Objects.equals(name, "images") && !Objects.equals(name, "webkit")){
            this.name = name;
            this.isSpecial = isSpecial;
        }
        try{
            ArrayList<String> lines = new ArrayList<>();
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while((line = reader.readLine())!=null){
                lines.add(line);
            }
            lines.remove(0);
            ChapterName = lines.get(0);
            reader.close();

            // 特殊情况讨论
            if(isSpecial){
                ArrayList<String> Replacements = new ArrayList<>();
                // 将String转换为char的ArrayList，方便索引补全
                ArrayList<Character> chars = new ArrayList<>();
                for(char ch:String.join("\n",lines).toCharArray()) chars.add(ch);

                // 循环判断是否是特殊字符
                int SpanTimes = -1;
                try{
                    for(int outside=0;outside<chars.size()-1;outside++){
                        Span span = new Span();

                        // 如果遇到了初始开始符
                        if(chars.get(outside)=='•' && chars.get(outside+1)=='<'){
                            // 将此时的迭代标识设为Span的开始索引
                            span.setStart(outside-1);
                            boolean FoundEnd = false;

                            // 嵌套循环从特殊字符之后的字符开始寻找初始结束符
                            inner:
                            for(int inside=outside+2;inside<chars.size()-1;inside++){
                                if(chars.get(inside)=='>' && chars.get(inside+1)=='•'){
                                    FoundEnd = true;
                                    // 将此时的嵌套迭代标识的下一位，即‘•’设为Span的结束索引
                                    span.setEnd(inside-2);
                                    // 将该Span添加到Spans里，并使Span计数加一
                                    spans.add(span);
                                    SpanTimes += 1;
                                    // 删除初始开始符
                                    for(int remove=0;remove<2;remove++) chars.remove(span.getStart()+1);
                                    // 删除初始结束符
                                    for(int remove=0;remove<2;remove++) chars.remove(span.getEnd());
                                    //将初始结束符的索引设为外部迭代标识符
                                    outside = span.getEnd();
                                    break inner;
                                }
                            }
                            if(!FoundEnd){
                                // 如果没找到初始结束符，自杀
                                Log.e(TAG,"No end(>•) found");
                                android.os.Process.killProcess(Process.myPid());
                                System.exit(1);
                            }

                        }else if(chars.get(outside)=='•' && chars.get(outside+1)=='&'){
                            // 如果找到了替换开始符
                            boolean FoundEnd = false;
                            StringBuilder sb = new StringBuilder();

                            // 嵌套循环寻找替换结束符
                            inner:
                            for (int inside = outside + 2; inside < chars.size()-1; inside++) {
                                if (chars.get(inside) == '&' && chars.get(inside + 1) == '•') {
                                    FoundEnd = true;
                                    // 删除文本之中要替换的字符
                                    if(inside+2==chars.size()){
                                        chars.subList(outside,inside).clear();
                                        for(int counter=0;counter<2;counter++) chars.remove(outside);
                                        outside -= 1;
                                    }else{
                                        chars.subList(outside, inside + 2).clear();
                                    }
                                    break inner;
                                }
                                // 将替换开始符之后的字符作为Replacement的值
                                sb.append(chars.get(inside));
                            }
                            // 删除多余空格
                            if(chars.get(outside)=='\n') chars.subList(outside-5,outside).clear();
                            if(chars.get(outside)==' ') {
                                chars.subList(outside - 3, outside).clear();
                                chars.remove(outside - 3);
                                outside = outside - 4;
                            }
                            if(!FoundEnd){
                                // 如果没找到替换结束符，自杀
                                Log.e(TAG,"No end(&•) found");
                                android.os.Process.killProcess(Process.myPid());
                                System.exit(1);
                            }
                            Replacements.add(sb.toString());
                            outside -= 1;
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    // 如果出现数组越界，自杀
                    Log.e(TAG,"Error processing the special content",new IndexOutOfBoundsException("处理特别章节的文本时出错"));
                    android.os.Process.killProcess(Process.myPid());
                    System.exit(1);
                }
                // 设置替换内容
                for(int index=0;index<spans.size();index++) spans.get(index).setReplacement(Replacements.get(index));
                // 合成Spannable内容
                StringBuilder sb = new StringBuilder();
                for(char ch:chars) sb.append(ch);
                content.append(sb.toString());
                // 为每个Span设置color并添加到Spannable
                for(Span span:spans) content.setSpan(span.getColor(),span.getStart(),span.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                result = String.join("\n",lines);
            }
        }catch (IOException e) {
            Log.e(TAG,"Error reading file:"+name,new IOException("阅读文件："+name+"时出错"));
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    // 设置章节名称
    public void setName(String name){this.name = name;}
    // 获取章节名称
    public String getName(){
        return name;
    }

    // 检查章节是否匹配
    public boolean Check(Activity activity) throws FileNotFoundException {
        final String TAG = "Chapter:Check(...)";
        CustomApplication app = (CustomApplication) activity.getApplication();
        boolean result = false;
        try {
            AssetManager assetManager = activity.getAssets();
            InputStream is = assetManager.open(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            // 删去乱码，并分情况讨论NovelName
            String NovelName;
            if(isSpecial){
                NovelName = br.readLine().trim().substring(1,6);
            }else{
                NovelName = br.readLine().trim().substring(1);
            }
            result = NovelName.equals(app.getNovelName());
        } catch (IOException e) {
            Log.e(TAG,"Error reading file:"+name,new IOException("阅读文件："+name+"时出错"));
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
        return result;
    }

    // 获取是否已读
    public boolean isRead() {
        return isRead;
    }

    // 设置是否已读
    public void setRead(boolean read) {
        isRead = read;
    }

    // 获取章节名称
    public String getChapterName() {
        return ChapterName;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public ArrayList<Span> getSpans() {
        final String TAG = "Chapter:getSpans()";
        if(isSpecial) return spans;
        else{
            // 如果非特殊章节调用该方法，自杀
            Log.e(TAG,"Caller isn't a special chapter");
            android.os.Process.killProcess(Process.myPid());
            System.exit(1);
            return null;
        }
    }

    public void setSpans(ArrayList<Span> spans){
        final String TAG = "Chapter:setSpans(...)";
        if(isSpecial) this.spans = spans;
        else{
            // 如果非特殊章节调用该方法，自杀
            Log.e(TAG,"Caller isn't a special chapter");
            android.os.Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }
    public SpannableStringBuilder getContent() {
        final String TAG = "Chapter:getContent()";
        if(isSpecial) return content;
        else{
            // 如果非特殊章节调用该方法，自杀
            Log.e(TAG,"Caller isn't a special chapter");
            android.os.Process.killProcess(Process.myPid());
            System.exit(1);
            return null;
        }
    }

    public String getResult(){return result;}
}
