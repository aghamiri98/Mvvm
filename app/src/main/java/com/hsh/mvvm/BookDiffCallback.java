package com.hsh.mvvm;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.hsh.mvvm.model.Book;

import java.util.ArrayList;

public class BookDiffCallback extends DiffUtil.Callback {

    private ArrayList<Book> oldBookList;
    private ArrayList<Book> newBookList;

    public BookDiffCallback(ArrayList<Book> oldBookList, ArrayList<Book> newBookList) {
        this.oldBookList = oldBookList;
        this.newBookList = newBookList;
    }

    @Override
    public int getOldListSize() {
        return oldBookList == null ? 0 : oldBookList.size();
    }

    @Override
    public int getNewListSize() {
        return newBookList == null ? 0 : newBookList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBookList.get(oldItemPosition).getBookId() == newBookList.get(newItemPosition).getBookId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBookList.get(oldItemPosition).equals(newBookList.get(newItemPosition).getBookId());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
