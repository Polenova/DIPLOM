package ru.android.polenova;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class NewNoteActivity extends AppCompatActivity {

    private EditText editTextName;
    private MultiAutoCompleteTextView editTextBody;
    private EditText editTextDate;
    private CheckBox checkBoxSelect;

    private String textName;
    private String textBody;
    private String textDate;
    private String textDateOfCreate;
    private boolean checkIsChecked;

    private Note getNote;
    private DatePickerDialog.OnDateSetListener onDateSet;
    private Bundle bundle;

    final Calendar dateDeadLine = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        this.setTitle("новые данные");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            getNote = (Note) bundle.getSerializable(Note.class.getSimpleName());
            textDateOfCreate = getNote.getDate().toString();
            editTextName.setText(getNote.getTextNameNote().toString());
            editTextBody.setText(getNote.getTextBodyNote().toString());
            editTextDate.setText(getNote.getTextDateNote().toString());
            checkBoxSelect.setChecked(getNote.isCheckIsChecked());
        }
    }

    private void initView() {
        editTextName = findViewById(R.id.editNameNote);
        editTextBody = findViewById(R.id.multiTextNote);
        editTextDate = findViewById(R.id.editDeadLine);
        checkBoxSelect = findViewById(R.id.checkBoxSelectDeadLine);
        checkBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if ("".equals(editTextDate.getText().toString())){
                        setDate();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Без крайней даты", Toast.LENGTH_SHORT).show();
                    String convertText = editTextDate.getText().toString();
                    editTextDate.setHint(convertText);
                    editTextDate.getText().clear();
                }
            }
        });
        ImageButton buttonDate = findViewById(R.id.imageButtonCalendar);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
    }

    public void setDate() {
        new DatePickerDialog(NewNoteActivity.this, date,
                dateDeadLine.get(Calendar.YEAR),
                dateDeadLine.get(Calendar.MONTH),
                dateDeadLine.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateDeadLine.set(Calendar.YEAR, year);
            dateDeadLine.set(Calendar.MONTH, monthOfYear);
            dateDeadLine.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    private void setInitialDate() {
        editTextDate.setText(DateUtils.formatDateTime(this,
                dateDeadLine.getTimeInMillis(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
        checkBoxSelect.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            Toast.makeText(NewNoteActivity.this, "save", Toast.LENGTH_LONG).show();
            saveInfoNote();
            return false;
        } else if (id == R.id.action_clear) {
            Toast.makeText(NewNoteActivity.this, "clear", Toast.LENGTH_LONG).show();
            editTextName.getText().clear();
            editTextBody.getText().clear();
            editTextDate.getText().clear();
            checkBoxSelect.setChecked(false);
            return false;
        } else if (id == android.R.id.home) {
            Intent intent = new Intent(NewNoteActivity.this, ListNoteActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void saveInfoNote() {
        if (bundle != null) {
            FileNotes.remove(this, getNote);
        }
        textName = editTextName.getText().toString();
        textBody = editTextBody.getText().toString();
        textDate = editTextDate.getText().toString();
        checkIsChecked = checkBoxSelect.isChecked();
        Note noteNewInfo = new Note(textName, textBody, textDate, checkIsChecked);
        try {
            FileNotes.exportToJSON(this, noteNewInfo);
            Toast.makeText(this, "готово", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }
}

