package com.example.androidapplicationv3.ui.request;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.converters.Converters;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.ui.MainActivity;
import com.example.androidapplicationv3.util.OnAsyncEventListener;
import com.example.androidapplicationv3.viewmodel.RequestViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsRequestActivity extends BaseActivity {

    private static final int EDIT_CLIENT = 1;
    private static final int DELETE_CLIENT = 2;

    private boolean isEditable;

    private TextView inputType;
    private TextView inputStatus;
    private EditText inputDateStart;
    private EditText inputDateEnd;
    private EditText inputRemarks;

    private RequestViewModel viewModel;
    private RequestWithType request;

    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_details_request, frameLayout);

        setTitle(getString(R.string.title_activity_detailsrequest));

        initiateView();

        Long requestId = getIntent().getLongExtra("requestId", 0);

        RequestViewModel.Factory factory = new RequestViewModel.Factory(getApplication(), requestId);
        viewModel = ViewModelProviders.of(this, factory).get(RequestViewModel.class);
        viewModel.getRequest().observe(this, requestEntity -> {
            if (requestEntity != null) {
                request = requestEntity;
                updateContent();
            }
        });

    }

    private void initiateView() {
        isEditable = false;
        inputType = findViewById(R.id.inputType);
        inputStatus = findViewById(R.id.inputStatus);
        inputDateStart = findViewById(R.id.inputDateStart);
        inputDateEnd = findViewById(R.id.inputDateEnd);
        inputRemarks = findViewById(R.id.inputRemarks);
    }

    private void updateContent() {
        if (request != null) {

            String dateDebut = new SimpleDateFormat("dd/MM/yyyy").format(request.request.getDateDebut());
            String dateFin = new SimpleDateFormat("dd/MM/yyyy").format(request.request.getDateFin());

            inputType.setText(request.type.getType());
            inputStatus.setText(request.status.getStatus());
            inputDateStart.setText(dateDebut);
            inputDateEnd.setText(dateFin);
            inputRemarks.setText(request.request.getRemark());
        }
    }

    private void switchEditableMode() throws ParseException {
        if (!isEditable) {
            inputDateStart.setFocusable(true);
            inputDateStart.setEnabled(true);
            inputDateStart.setFocusableInTouchMode(true);

            inputDateEnd.setFocusable(true);
            inputDateEnd.setEnabled(true);
            inputDateEnd.setFocusableInTouchMode(true);

            inputRemarks.setFocusable(true);
            inputRemarks.setEnabled(true);
            inputRemarks.setFocusableInTouchMode(true);
            inputDateStart.requestFocus();

        } else {
            saveChanges(
                    inputDateStart.getText().toString(),
                    inputDateEnd.getText().toString(),
                    inputRemarks.getText().toString()
            );

            inputDateStart.setFocusable(false);
            inputDateStart.setEnabled(false);
            inputDateEnd.setFocusable(false);
            inputDateEnd.setEnabled(false);
            inputRemarks.setFocusable(false);
            inputRemarks.setEnabled(false);
        }
        isEditable = !isEditable;
    }


    private void saveChanges(String dateStart, String dateEnd, String remark) throws ParseException {
       /* if (!pwd.equals(pwd2) || pwd.length() < 5) {
            toast = Toast.makeText(this, getString(R.string.error_edit_invalid_password), Toast.LENGTH_LONG);
            toast.show();
            return;
        }*/
        /*if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_invalid_email));
            etEmail.requestFocus();
            return;
        }*/


        Converters converters = new Converters();

        Date dateDebut = new SimpleDateFormat("dd/MM/yyyy").parse(dateStart);
        Date dateFin = new SimpleDateFormat("dd/MM/yyyy").parse(dateEnd);

        request.request.setDateDebut(converters.dateToTimestamp(dateDebut));
        request.request.setDateFin(converters.dateToTimestamp(dateFin));
        request.request.setRemark(remark);
        request.request.setIdStatus(new Long(1));

        viewModel.updateRequest(request.request, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                setResponse(false);
            }
        });
    }

    private void setResponse(Boolean response) {
        if (response) {
            updateContent();
            toast = Toast.makeText(this, getString(R.string.request_edited), Toast.LENGTH_LONG);
            toast.show();
        } else {
            toast = Toast.makeText(this, getString(R.string.request_edited_error), Toast.LENGTH_LONG);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, EDIT_CLIENT, Menu.NONE, getString(R.string.action_edit))
                .setIcon(R.drawable.ic_baseline_edit_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, DELETE_CLIENT, Menu.NONE, getString(R.string.action_delete))
                .setIcon(R.drawable.ic_baseline_delete_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == EDIT_CLIENT) {
            if (isEditable) {
                item.setIcon(R.drawable.ic_baseline_edit_24);
                try {
                    switchEditableMode();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                item.setIcon(R.drawable.ic_baseline_save_alt_24);
                try {
                    switchEditableMode();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (item.getItemId() == DELETE_CLIENT) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.action_delete));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.delete_msg));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_delete), (dialog, which) -> {
                viewModel.deleteRequest(request.request, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        logout();
                    }

                    @Override
                    public void onFailure(Exception e) {}
                });
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }




}