package ga.josepolanco.dondeestaeldelito02.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.MediaPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ga.josepolanco.dondeestaeldelito02.MainActivity;
import ga.josepolanco.dondeestaeldelito02.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TercerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TercerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TercerFragment extends Fragment {


    public interface IDevolverUrlFoto{
        public void urlFoto(String url);

    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View nView;
    private FusedLocationProviderClient mFusedLocationClient;
    double latitud = 0, longitud = 0;
    private boolean isPermission;
    private FusedLocationProviderClient FusedLocationClient;
    private TextView denuncia_ubicacion, denuncia_nombre, denuncia_hora, denuncia_descripcion, urlFotoPerfil, n_longitud, n_latitud;
    private ImageView denuncia_foto_perfil, result;
    private RadioGroup rb_grupo1;
    private RadioButton rb_accidente,rb_asesinato,rb_incendio, rb_pelea, rb_robo;
    public String rbTipo="";

    DatabaseReference mRootReference;
    private FirebaseStorage storage;
    private StorageReference storageImagenDenuncia;

    private Button btn_denuncia_publicar,btn_capturar, btn_denuncia_capturar02;

    static int MY_REQUEST_CODE=1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imagen_denuncia_uri;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    private ProgressDialog progressDialog;

    public TercerFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TercerFragment newInstance(String param1, String param2) {
        TercerFragment fragment = new TercerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        nView = inflater.inflate(R.layout.fragment_tercer, container, false);

        denuncia_foto_perfil = nView.findViewById(R.id.denuncia_foto_perfil);
        denuncia_ubicacion = nView.findViewById(R.id.denuncia_ubicacion);
        denuncia_nombre = nView.findViewById(R.id.denuncia_nombre);
        denuncia_hora = nView.findViewById(R.id.denuncia_hora);
        rb_grupo1 = nView.findViewById(R.id.rb_grupo1);
        rb_accidente = nView.findViewById(R.id.rb_accidente);
        rb_asesinato = nView.findViewById(R.id.rb_asesinato);
        rb_pelea = nView.findViewById(R.id.rb_pelea);
        rb_robo = nView.findViewById(R.id.rb_robo);
        denuncia_descripcion = nView.findViewById(R.id.denuncia_descripcion);
        btn_denuncia_publicar = nView.findViewById(R.id.btn_denuncia_publicar);
        btn_denuncia_capturar02 = nView.findViewById(R.id.btn_denuncia_capturar02);
        result=nView.findViewById(R.id.result);
        n_latitud = nView.findViewById(R.id.n_latitud);
        n_longitud = nView.findViewById(R.id.n_longitud);
        imagePicker = new ImagePicker(this);
        cameraPicker = new CameraImagePicker(this);

        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);

        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if (!list.isEmpty()){
                    String path = list.get(0).getOriginalPath();
                    imagen_denuncia_uri = Uri.fromFile(new File(path));
                    result.setImageURI(imagen_denuncia_uri);
                }
            }
            @Override
            public void onError(String s) {
                Toast.makeText(getContext(), "Error ImagePicker",Toast.LENGTH_SHORT).show();
            }
        });

        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                    String path = list.get(0).getOriginalPath();
                    imagen_denuncia_uri = Uri.fromFile(new File(path));
                    result.setImageURI(imagen_denuncia_uri);
            }

            @Override
            public void onError(String s) {
                Toast.makeText(getContext(), "Error CameraPicker",Toast.LENGTH_SHORT).show();
            }
        });

        btn_denuncia_capturar02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Fotografia del Delito");

                String[] items = new String[]{"Galeria","Camara"};

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                imagePicker.pickImage(); //inicializamos el calculo.
                                break;
                            case 1:
                                pickerPath = cameraPicker.pickImage();
                                break;
                        }
                    }
                });

                AlertDialog dialogConstruido = dialog.create();
                dialogConstruido.show();
            }
        });


        rb_grupo1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rb_accidente){
                    rbTipo = rb_accidente.getText().toString();
                } else  if (checkedId == R.id.rb_asesinato) {
                    rbTipo = rb_asesinato.getText().toString();
                } else  if (checkedId == R.id.rb_pelea) {
                    rbTipo = rb_pelea.getText().toString();
                }else{
                    rbTipo = rb_robo.getText().toString();
//                    Toast.makeText(getContext(), "valor: "+rb_robo.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            String nombresss = user.getDisplayName();
            String emailsss = user.getEmail();
            String facebookUserId = user.getUid();
            Uri foto = user.getPhotoUrl();
            for(UserInfo profile : user.getProviderData()) {
                facebookUserId = profile.getUid();
            }

            Date date = Calendar.getInstance().getTime();
            DateFormat df = new SimpleDateFormat("HH:mm a  -  dd/MM/yyyy");
            String strDate = df.format(date);

            denuncia_nombre.setText(nombresss);
            denuncia_hora.setText(strDate);
            Picasso.get().load(user.getPhotoUrl()).resize(100,100).error(R.drawable.ic_imagen_error).into(denuncia_foto_perfil);

        }else{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (chequearPermiso()) return nView;

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
//                                    Log.e(" Latitud: ",+location.getLatitude()+" Longitud: "+location.getLongitude());
                                    latitud = location.getLatitude();
                                    longitud = location.getLongitude();
                                    n_latitud.setText(String.valueOf(latitud));
                                    n_longitud.setText(String.valueOf(longitud));
                                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> addresses = null;
                                    try {
                                        addresses = geocoder.getFromLocation(latitud, longitud, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String cityName = addresses.get(0).getAddressLine(0);
                                    denuncia_ubicacion.setText("En "+cityName);
                                }
                            }
                        });
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(-79.0431466,-79.0431466))
                        .zoom(15) //tamaño de la vista
                        .bearing(0) //direccion de la camara
                        .tilt(45) //angulo de la camara
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null); //distancia de acercamiento
            }
        });

        storage = FirebaseStorage.getInstance();
        storageImagenDenuncia = storage.getReference("Imagenes/Denuncias/"+user.getUid());

        mRootReference = FirebaseDatabase.getInstance().getReference();

        btn_denuncia_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagen_denuncia_uri!=null){
                    final ProgressDialog progress = new ProgressDialog(getContext());
                    progress.setTitle("Publicar Denuncia");
                    progress.setMessage("Subiendo Fotografia...");
                    progress.setMessage("Publicando Incidente...");
                    progress.show();
                    progress.setCancelable(false);
                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            progress.cancel();
                        }
                    };
                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 15000);
                    subirFotoDenuncia(imagen_denuncia_uri, new IDevolverUrlFoto() {
                        @Override
                        public void urlFoto(String url) {
                            String delito_nombre = denuncia_nombre.getText().toString();
                            String delito_ubicacion = denuncia_ubicacion.getText().toString();
                            String delito_hora = denuncia_hora.getText().toString();
                            String tipo_delito = rbTipo;
                            String descripcion = denuncia_descripcion.getText().toString();
                            String new_latitud = n_latitud.getText().toString();
                            double renew_latitud = Double.parseDouble(new_latitud);
                            String new_longitud = n_longitud.getText().toString();
                            double renew_longitud = Double.parseDouble(new_longitud);
                            final Map<String, Object> datosDenuncia = new HashMap<>();
                            datosDenuncia.put("nombre",delito_nombre);
                            datosDenuncia.put("ubicacion",delito_ubicacion);
                            datosDenuncia.put("fecha",delito_hora);
                            datosDenuncia.put("tipo_delito",tipo_delito);
                            datosDenuncia.put("descripcion",descripcion);
                            datosDenuncia.put("urlImagen",url);
                            datosDenuncia.put("urlFotoPerfil", user.getPhotoUrl().toString());
                            datosDenuncia.put("latitud",renew_latitud);
                            datosDenuncia.put("longitud",renew_longitud);
//                            Toast.makeText(getContext(), "uri: "+url,Toast.LENGTH_SHORT).show();
                            if (tipo_delito!=null &&!tipo_delito.isEmpty()){
                                if (descripcion != null && !descripcion.isEmpty()){
                                    mRootReference.child("Denuncias").push().setValue(datosDenuncia);
                                    denuncia_descripcion.setText("");
                                    result.setImageDrawable(null);
                                    rb_grupo1.clearCheck();
//                                    Toast.makeText(getContext(),"Denuncia Registrada",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getContext(),"Ingrese la descripcion",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getContext(),"Seleccione el tipo de delito",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    final ProgressDialog progress = new ProgressDialog(getContext());
                    progress.setTitle("Publicar Denuncia");
                    progress.setMessage("Publicando Incidente...");
                    progress.show();
                    progress.setCancelable(false);
                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            progress.cancel();
                        }
                    };
                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 1000);
                    String delito_nombre = denuncia_nombre.getText().toString();
                    String delito_ubicacion = denuncia_ubicacion.getText().toString();
                    String delito_hora = denuncia_hora.getText().toString();
                    String tipo_delito = rbTipo;
                    String descripcion = denuncia_descripcion.getText().toString();
                    String new_latitud = n_latitud.getText().toString();
                    double renew_latitud = Double.parseDouble(new_latitud);
                    String new_longitud = n_longitud.getText().toString();
                    double renew_longitud = Double.parseDouble(new_longitud);
                    final Map<String, Object> datosDenuncia = new HashMap<>();
                    datosDenuncia.put("nombre",delito_nombre);
                    datosDenuncia.put("ubicacion",delito_ubicacion);
                    datosDenuncia.put("fecha",delito_hora);
                    datosDenuncia.put("tipo_delito",tipo_delito);
                    datosDenuncia.put("descripcion",descripcion);
                    datosDenuncia.put("urlFotoPerfil", user.getPhotoUrl().toString());
                    datosDenuncia.put("latitud",renew_latitud);
                    datosDenuncia.put("longitud",renew_longitud);
                    if (tipo_delito!=null &&!tipo_delito.isEmpty()){
                        if (descripcion != null && !descripcion.isEmpty()){
                            mRootReference.child("Denuncias").push().setValue(datosDenuncia);
                            denuncia_descripcion.setText("");
                            rb_grupo1.clearCheck();
//                            Toast.makeText(getContext(),"Denuncia Registrada",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext(),"Ingrese la descripcion",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(),"Seleccione el tipo de delito",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return nView;
    }


    public void subirFotoDenuncia(Uri uri, final IDevolverUrlFoto idevolverUrlFoto){
        String nombrefoto = "";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss-mm-hh-dd-MM-yyyy", Locale.getDefault());
        nombrefoto = simpleDateFormat.format(date);
        final StorageReference fotoReferencia = storageImagenDenuncia.child(nombrefoto);
        fotoReferencia.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return fotoReferencia.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri uri = task.getResult();
                    idevolverUrlFoto.urlFoto(uri.toString());
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK){
            imagePicker.submit(data);
        }else if(requestCode==Picker.PICK_IMAGE_CAMERA && resultCode==RESULT_OK){
            cameraPicker.reinitialize(pickerPath);
            cameraPicker.submit(data);
        }
    }


    private boolean chequearPermiso() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
//                            Log.e(" Latitud: ",+location.getLatitude()+" Longitud: "+location.getLongitude());
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                        }
                    }
                });
        return false;
    }


    private boolean requestSinglePermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(getContext(), "Single permission is granted!", Toast.LENGTH_SHORT).show();
                        isPermission = true;
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            isPermission = false;
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        return isPermission;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onActivityCreated(savedInstanceState);
    }
}
