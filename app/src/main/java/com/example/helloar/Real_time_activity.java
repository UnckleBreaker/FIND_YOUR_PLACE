package com.example.helloar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.ArrayList;
import java.util.List;

public class Real_time_activity extends AppCompatActivity {
    private ArFragment arFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_time_activity);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arfragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            createViewRendarable(hitResult.createAnchor());
        });
    }

        private void createViewRendarable(Anchor anchor) {

            ViewRenderable
                    .builder()
                    .setView(this,R.layout.test)
                    .build()
                    .thenAccept(viewRenderable -> {
                        addToScene(viewRenderable,anchor);
                    });
        }
        private void addToScene(ViewRenderable viewRenderable, Anchor anchor) {
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setRenderable(viewRenderable);
            arFragment.getArSceneView().getScene().addChild(anchorNode);

            View view = viewRenderable.getView();

            ViewPager viewPager = view.findViewById(R.id.viewPager);
            List<Integer> images = new ArrayList<>();
            images.add(R.drawable.ic_launcher_background);
            images.add(R.drawable.ic_launcher_foreground);
            images.add(R.drawable.ic_launcher_background);
            images.add(R.drawable.ic_launcher_foreground);

            Adapter adapter =new Adapter(images);
            viewPager.setAdapter(adapter);


        }
        private class Adapter extends PagerAdapter
        {
            List<Integer> images;

            Adapter(List<Integer> images){
                this.images=images;

            }

            @Override
            public int getCount() {
                return images.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((ImageView)object);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = getLayoutInflater().inflate(R.layout.item,container,false);
                ImageView imageView = view.findViewById(R.id.Image_View);
                imageView.setImageResource(images.get(position));

                container.addView(view);
                return view;
            }
        }
    }

