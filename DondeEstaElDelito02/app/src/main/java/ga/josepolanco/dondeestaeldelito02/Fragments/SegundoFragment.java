package ga.josepolanco.dondeestaeldelito02.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ga.josepolanco.dondeestaeldelito02.Clases.Denuncias;
import ga.josepolanco.dondeestaeldelito02.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SegundoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SegundoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SegundoFragment extends Fragment {
    View nView;

    private boolean isPermission;
    private FusedLocationProviderClient FusedLocationClient;
    double d_latitud, d_longitud;
    private FusedLocationProviderClient mFusedLocationClient;
    String titulo, descripcion;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SegundoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SegundoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SegundoFragment newInstance(String param1, String param2) {
        SegundoFragment fragment = new SegundoFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        nView = inflater.inflate(R.layout.fragment_segundo, container, false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                mMap.clear(); //clear old markers

                database.getReference().child("Denuncias").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            d_latitud = snapshot.child("latitud").getValue(Double.class);
                            d_longitud = snapshot.child("longitud").getValue(Double.class);
                            titulo = snapshot.child("tipo_delito").getValue(String.class);
                            descripcion = snapshot.child("descripcion").getValue(String.class);
                            LatLng latLng = new LatLng(d_latitud,d_longitud);
                            String info_marker= descripcion;
                            if (titulo.equalsIgnoreCase("Accidente")){
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(info_marker)
                                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_accidente",100,100))));

                            }if (titulo.equalsIgnoreCase("Asesinato")){
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(info_marker)
                                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_asesinato",100,100))));

                            }if (titulo.equalsIgnoreCase("Pelea")){
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(info_marker)
                                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_pelea",100,100))));

                            }if (titulo.equalsIgnoreCase("Robo")){
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(info_marker)
                                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_robo",100,100))));

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    Log.e(" Latitud: ",+location.getLatitude()+" Longitud: "+location.getLongitude());
                                    double latitud = location.getLatitude();
                                    double longitud = location.getLongitude();
                                    CameraPosition googlePlex = CameraPosition.builder()
                                            .target(new LatLng(latitud,longitud))
                                            .zoom(15) //tamaño de la vista
                                            .bearing(0) //direccion de la camara
                                            .tilt(45) //angulo de la camara
                                            .build();
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null); //distancia de acercamiento

                                }else{
                                    CameraPosition googlePlex = CameraPosition.builder()
                                            .target(new LatLng(-8.1117789,-79.0286686))
                                            .zoom(15) //tamaño de la vista
                                            .bearing(0) //direccion de la camara
                                            .tilt(45) //angulo de la camara
                                            .build();
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null); //distancia de acercamiento

                                }
                            }
                        });
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(-8.1285728,-79.0431466))
                        .title("Universidad Cesar Vallejo")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ucv",50,50)))
                        .snippet(""));
            }
        });

        return nView;
    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
