package com.example.section_recycler_view.dataSet;

import com.example.section_recycler_view.library.Section;

import java.util.List;

public class SectionHeader implements Section<Child>, Comparable<SectionHeader> {

    public List<Child> childList;
    String sectionText;
    String total;
    int index;


    public SectionHeader(List<Child> childList, String sectionText, String total) {
        this.childList = childList;
        this.sectionText = sectionText;
        this.total = total;

    }


    @Override
    public List<Child> getChildItems() {
        return childList;
    }

    public String getTotal() {
        return total;
    }

    public String getSectionText() {
        return sectionText;
    }

    @Override
    public int compareTo(SectionHeader another) {
        if (this.index > another.index) {
            return -1;
        } else {
            return 1;
        }
    }
}