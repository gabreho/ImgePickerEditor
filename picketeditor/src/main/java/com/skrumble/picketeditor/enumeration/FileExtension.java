package com.skrumble.picketeditor.enumeration;

import com.skrumble.picketeditor.R;

public enum FileExtension {
    Unknow(""), NoNCategory(""),
    Png("png"), Jpg("jpg"), Jpeg("jpeg"), Bmp("bmp"), Svg("svg"), Gif("gif"),
    Pdf("pdf"), Psd("psd"), Ai("ai"), Eps("eps"),
    Html("html"), Css("css"), Xml("xml"), Php("php"), Sql("sql"), Js("js"),
    Dmg("dmg"), Exe("exe"), Iso("iso"), Txt("txt"),
    SevenZ("7z"), Zip("zip"), Rar("rar"),
    Wav("wav"), Mp3("mp3"), Aac("aac"), Amr("amr"),
    Wmv("wmv"), Mp4("mp4"), Mpg("mpg"), M4v("m4v"), Mov("mov"), Avi("avi"),
    Xls("xls"), Doc("doc"), Ppt("ppt"), Xlsx("xlsx"), Docx("docx"), Pptx("pptx"),
    Csv("csv");

    public String ext;

    FileExtension(String ext) {
        this.ext = ext;
    }

    public static FileExtension fromString(String s) {

        for (FileExtension extension : FileExtension.values()) {

            if (extension.ext.equals(s.toLowerCase())) {
                return extension;
            }
        }

        if (s.isEmpty()) {
            return Unknow;
        }

        NoNCategory.ext = s;

        return NoNCategory;
    }

    public int getImageIconSource() {
        if (this == SevenZ) {
            return R.drawable.ic_and_file_icn_7z;
        }
        if (this == Aac) {
            return R.drawable.ic_and_file_icn_aac;
        }
        if (this == Ai) {
            return R.drawable.ic_and_file_icn_ai;
        }
        if (this == Avi) {
            return R.drawable.ic_and_file_icn_avi;
        }
        if (this == Bmp) {
            return R.drawable.ic_and_file_icn_bmp;
        }
        if (this == Css) {
            return R.drawable.ic_and_file_icn_css;
        }
        if (this == Dmg) {
            return R.drawable.ic_and_file_icn_dmg;
        }
        if (this == Doc || this == Docx) {
            return R.drawable.ic_and_file_icn_doc;
        }
        if (this == Eps) {
            return R.drawable.ic_and_file_icn_eps;
        }
        if (this == Exe) {
            return R.drawable.ic_and_file_icn_exe;
        }
        if (this == Gif) {
            return R.drawable.ic_and_file_icn_gif;
        }
        if (this == Html) {
            return R.drawable.ic_and_file_icn_html;
        }
        if (this == Iso) {
            return R.drawable.ic_and_file_icn_iso;
        }
        if (this == Jpg || this == Jpeg) {
            return R.drawable.ic_and_file_icn_jpg;
        }
        if (this == Js) {
            return R.drawable.ic_and_file_icn_js;
        }
        if (this == M4v) {
            return R.drawable.ic_and_file_icn_m4v;
        }
        if (this == Mov) {
            return R.drawable.ic_and_file_icn_mov;
        }
        if (this == Mp3) {
            return R.drawable.ic_and_file_icn_mp3;
        }
        if (this == Mp4) {
            return R.drawable.ic_and_file_icn_mp4;
        }
        if (this == Mpg) {
            return R.drawable.ic_and_file_icn_mpg;
        }
        if (this == Pdf) {
            return R.drawable.ic_and_file_icn_pdf;
        }
        if (this == Php) {
            return R.drawable.ic_and_file_icn_php;
        }
        if (this == Png) {
            return R.drawable.ic_and_file_icn_png;
        }
        if (this == Ppt || this == Pptx) {
            return R.drawable.ic_and_file_icn_ppt;
        }
        if (this == Psd) {
            return R.drawable.ic_and_file_icn_psd;
        }
        if (this == Rar) {
            return R.drawable.ic_and_file_icn_rar;
        }
        if (this == Sql) {
            return R.drawable.ic_and_file_icn_sql;
        }
        if (this == Svg) {
            return R.drawable.ic_and_file_icn_svg;
        }
        if (this == Txt) {
            return R.drawable.ic_and_file_icn_txt;
        }
        if (this == Wav) {
            return R.drawable.ic_and_file_icn_wav;
        }
        if (this == Wmv) {
            return R.drawable.ic_and_file_icn_wmv;
        }
        if (this == Xls || this == Xlsx) {
            return R.drawable.ic_and_file_icn_xls;
        }
        if (this == Xml) {
            return R.drawable.ic_and_file_icn_xml;
        }
        if (this == Zip) {
            return R.drawable.ic_and_file_icn_zip;
        }
        return R.drawable.ic_and_file_icn_unknown;
    }
}