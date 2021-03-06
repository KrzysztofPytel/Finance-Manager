package appzaliczenie.financemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements DatabaseOperations {

    private EditText userNameET, passwordET;
    private CheckBox rememberLoginAndPassword;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private CheckConnection cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameET = (EditText) findViewById(R.id.loginET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        rememberLoginAndPassword = (CheckBox) findViewById(R.id.saveLoginCheckBox);

        sp = getPreferences(Context.MODE_PRIVATE);
        edit = sp.edit();
        restoreLoginAndPassword();
        cc = new CheckConnection(this);
    }


    public void createAccount(View view) {
        if(!cc.isNetworkConnected()){
            new Toast("Brak polaczenia z internetem", this);
        }else {
            Intent intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);
        }
    }

    public void onLogin(View view){
        if(!cc.isNetworkConnected()){
            new Toast("Brak polaczenia z internetem", this);
        }else {
            String userName = userNameET.getText().toString();
            String password = passwordET.getText().toString();

            if (rememberLoginAndPassword.isChecked())
                saveLoginAndPassword();
            else
                clearSavedData();

            BackgroundWorker loginWorker = new BackgroundWorker(this);
            loginWorker.execute(LOGIN, userName, password);
        }
    }

    private void restoreLoginAndPassword(){
        String savedLogin = sp.getString("login", "");
        String savedPassword = sp.getString("password", "");

        userNameET.setText(savedLogin);
        passwordET.setText(savedPassword);

        rememberLoginAndPassword.setChecked(true);
    }

    private void saveLoginAndPassword(){
        edit.putString("login", userNameET.getText().toString());
        edit.putString("password", passwordET.getText().toString());
        edit.apply();
    }

    private void clearSavedData(){
        edit.putString("login", "");
        edit.putString("password", "");
        edit.apply();
    }

}
