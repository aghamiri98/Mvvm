package com.hsh.mvvm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hsh.mvvm.databinding.BookListItemBinding;
import com.hsh.mvvm.model.Book;

import java.util.ArrayList;

public class BooksAdapters extends RecyclerView.Adapter<BooksAdapters.BookViewHolder> {
    private OnItemClickListener onItemClickListener;
    private ArrayList<Book> bookArrayList = new ArrayList<>();

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookListItemBinding bookListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.book_list_item, parent, false);
        return new BookViewHolder(bookListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookArrayList.get(position);
        holder.bookListItemBinding.setBook(book);

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        BookListItemBinding bookListItemBinding;


        public BookViewHolder(@NonNull BookListItemBinding bookListItemBinding) {
            super(bookListItemBinding.getRoot());
            this.bookListItemBinding = bookListItemBinding;
            bookListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickPosition = getAdapterPosition();
                    if (onItemClickListener != null && clickPosition != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(bookArrayList.get(clickPosition));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setBookArrayList(ArrayList<Book> bookArrayList) {
        this.bookArrayList = bookArrayList;
        notifyDataSetChanged();
    }

    public void setBooks(ArrayList<Book> newBookArrayList) {
         DiffUtil.DiffResult result =DiffUtil.calculateDiff(new BookDiffCallback(bookArrayList,newBookArrayList),false);
         bookArrayList=newBookArrayList;
         result.dispatchUpdatesTo(BooksAdapters.this);

    }



}
