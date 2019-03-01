package com.skrumble.picketeditor.enumeration;

public enum FileExtension {
    Unknow(""),
    Png("png"), Jpg("jpg"), Jpeg("jpeg"), Bmp("bmp"), Svg("svg"), Gif("gif"),
    Pdf("pdf"), Psd("psd"), Ai("ai"), Eps("eps"),
    Html("html"), Css("css"), Xml("xml"), Php("php"), Sql("sql"), Js("js"),
    Dmg("dmg"), Exe("exe"), Iso("iso"), Txt("txt"),
    SevenZ("7z"), Zip("zip"), Rar("rar"),
    Wav("wav"), Mp3("mp3"), Aac("aac"), Amr("amr"),
    Wmv("wmv"), Mp4("mp4"), Mpg("mpg"), M4v("m4v"), Mov("mov"), Avi("avi"),
    Xls("xls"), Doc("doc"), Ppt("ppt"), Xlsx("xlsx"), Docx("docx"), Pptx("pptx"),
    Csv("csv");

    String ext;

    FileExtension(String ext){
        this.ext = ext;
    }


}
