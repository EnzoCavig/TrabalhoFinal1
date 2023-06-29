package com.example.myapplication;
package br.edu.unidavi.bsn.activities;

public class FormularioActivity {


import java.io.Serializable;
import com.example.listagemdealunos.R;
import br.edu.unidavi.bsn.dao.AlunoDAO;
import br.edu.unidavi.bsn.helpers.FormularioHelper;
import br.edu.unidavi.bsn.model.Aluno;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

    public class FormularioActivity extends Activity {
        private String localArquivoFoto;
        private static final int TIRA_FOTO = 123;

        private FormularioHelper helper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_formulario);

            Intent intent = getIntent();
            final Aluno alunoParaSerAlterado = (Aluno) intent.getSerializableExtra("alunoSelecionado");

            this.helper = new FormularioHelper(this);

            Button botao = (Button) findViewById(R.id.botao);

            if (alunoParaSerAlterado != null) {
                botao.setText("Alterar");
                helper.colocaAlunoNoFormulario(alunoParaSerAlterado);
                Toast.makeText(this, "Aluno: "+alunoParaSerAlterado.getNome(), Toast.LENGTH_SHORT).show();
            }

            botao.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Aluno aluno = helper.pegaAlunoDoFormulario();

                    AlunoDAO dao = new AlunoDAO(FormularioActivity.this);

                    if (alunoParaSerAlterado == null) {
                        dao.insere(aluno);
                    } else {
                        aluno.setId(alunoParaSerAlterado.getId());
                        dao.altera(aluno);
                    }

                    dao.close();

                    finish();

                    Toast.makeText(FormularioActivity.this, "Objeto aluno criado!",
                            Toast.LENGTH_SHORT).show();
                }
            });

            ImageView foto = helper.getBotaoImagem();
            foto.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    localArquivoFoto = Environment.getExternalStorageDirectory() +
                            "/"+ System.currentTimeMillis()+".jpg";

                    File arquivo = new File(localArquivoFoto);
                    Uri localFoto = Uri.fromFile(arquivo);

                    Intent irParaCamera =
                            new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);
                    startActivityForResult(irParaCamera, TIRA_FOTO);

                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == TIRA_FOTO) {
                if (resultCode == Activity.RESULT_OK) {
                    helper.carregaImagem(this.localArquivoFoto);
                } else {
                    this.localArquivoFoto = null;
                }
            }
        }
    }

}
    }



    Para o projeto funcionar corretamente é necessário editar o arquivo AndroidManifest.xml. Observe abaixo as linhas em negrito adicionadas para configurar a nova activity criada.


    <?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
            package="com.example.listagemdealunos"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk
    android:minSdkVersion="8"
    android:targetSdkVersion="19" />

    <application
    android:allowBackup="true"
    android:icon="@drawable/ic_launcher"
    android:label="@string/app_name"
    android:theme="@style/AppTheme" >
        <activity
    android:name="br.edu.unidavi.bsn.activities.FormularioActivity"
    android:label="@string/app_name" />
        <activity
    android:name="br.edu.unidavi.bsn.activities.ListaAlunosActivity"
    android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
    android:name="br.edu.unidavi.bsn.activities.FormularioActivity"
    android:label="@string/title_activity_formulario" >
        </activity>
    </application>

</manifest>

}
