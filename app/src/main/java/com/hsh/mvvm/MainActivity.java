package com.hsh.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hsh.mvvm.databinding.ActivityMainBinding;
import com.hsh.mvvm.model.Book;
import com.hsh.mvvm.model.Category;
import com.hsh.mvvm.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.hsh.mvvm.AddAndEditActivity.BOOK_ID;
import static com.hsh.mvvm.AddAndEditActivity.BOOK_NAME;
import static com.hsh.mvvm.AddAndEditActivity.UNIT_PRICE;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private MainActivityClickHandlers handlers;

    private ArrayList<Category> categoryArrayList;
    private ArrayList<Book> bookArrayList;

    private Category selectedCategory;

    private RecyclerView recyclerView;
    private BooksAdapters booksAdapters;

    public static final int ADD_BOOK_REQUEST = 1;
    public static final int EDIT_BOOK_REQUEST = 2;

    private int selectedCategoryId;
    private int selectedBookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        handlers = new MainActivityClickHandlers();
        activityMainBinding.setClickHandlers(handlers);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mainActivityViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryArrayList = (ArrayList<Category>) categories;
                showOnSpinner();
            }
        });


    }

    private void showOnSpinner() {
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(this, R.layout.support_simple_spinner_dropdown_item, categoryArrayList);
        categoryArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        activityMainBinding.setSpinnerAdapter(categoryArrayAdapter);

    }

    private void loadBooksArrayList(int categoryId) {
        mainActivityViewModel.getBooksOfSelectedCategory(categoryId).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                bookArrayList = (ArrayList<Book>) books;
                loadRecyclerView();
            }
        });
    }

    private void loadRecyclerView() {
        recyclerView = activityMainBinding.secondaryLayout.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        booksAdapters = new BooksAdapters();
        recyclerView.setAdapter(booksAdapters);
        //booksAdapters.setBookArrayList(bookArrayList);
        booksAdapters.setBooks(bookArrayList);
        booksAdapters.setOnItemClickListener(new BooksAdapters.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                selectedBookId = book.getBookId();
                Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);
                intent.putExtra(BOOK_ID, selectedBookId);
                intent.putExtra(BOOK_NAME, book.getBookName());
                intent.putExtra(UNIT_PRICE, book.getUnitPrice());
                startActivityForResult(intent, EDIT_BOOK_REQUEST);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Book book = bookArrayList.get(viewHolder.getAdapterPosition());
                mainActivityViewModel.deleteBook(book);


            }
        }).attachToRecyclerView(recyclerView);

    }

    public class MainActivityClickHandlers {
        public void onFabClick(View view) {
            Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);
            startActivityForResult(intent, ADD_BOOK_REQUEST);
        }


        public void onSelectedItem(AdapterView<?> parent, View view, int pos, long id) {
            selectedCategory = (Category) parent.getItemAtPosition(pos);
            loadBooksArrayList(selectedCategory.getId());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        selectedCategoryId = selectedCategory.getId();

        if (requestCode == ADD_BOOK_REQUEST && resultCode == RESULT_OK) {

            Book book = new Book();
            book.setCategoryId(selectedCategoryId);
            book.setBookName(data.getStringExtra(BOOK_NAME));
            book.setUnitPrice(data.getStringExtra(UNIT_PRICE));
            mainActivityViewModel.addNewBook(book);

        } else if (requestCode == EDIT_BOOK_REQUEST && resultCode == RESULT_OK) {
            Book book = new Book();
            book.setCategoryId(selectedCategoryId);
            book.setBookId(selectedBookId);
            book.setBookName(data.getStringExtra(BOOK_NAME));
            book.setUnitPrice(data.getStringExtra(UNIT_PRICE));
            mainActivityViewModel.updateBook(book);

        }


    }
}
