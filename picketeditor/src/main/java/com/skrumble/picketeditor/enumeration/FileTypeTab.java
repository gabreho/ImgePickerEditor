package com.skrumble.picketeditor.enumeration;

import android.content.Context;

import com.skrumble.picketeditor.R;

public enum  FileTypeTab {
    ALL(R.string.all),
    PDF(R.string.pdf),
    DOC(R.string.doc),
    ZIP(R.string.zip),
    OTHERS(R.string.others);

    public int title;

    FileTypeTab(int title){
        this.title = title;
    }

    public static FileTypeTab getFromString(Context context, String toString) {
        for (FileTypeTab tab : FileTypeTab.values()){
            if (context.getString(tab.title).equals(toString)){
                return tab;
            }
        }
        return ALL;
    }
}
