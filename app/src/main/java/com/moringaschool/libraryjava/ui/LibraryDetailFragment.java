package com.moringaschool.libraryjava.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.ebeautyparlor.Constants;
import com.moringaschool.ebeautyparlor.R;
import com.moringaschool.ebeautyparlor.models.BeautyParlor;
import com.moringaschool.ebeautyparlor.models.Parlor;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryDetailFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.parlorImageView)
    ImageView mImageLabel;
    @BindView(R.id.parlorNameTextView)
    TextView mNameLabel;
    @BindView(R.id.styleTextView)
    TextView mCategoriesLabel;
    @BindView(R.id.ratingTextView)
    TextView mRatingLabel;
    @BindView(R.id.websiteTextView)
    TextView mWebsiteLabel;
    @BindView(R.id.phoneTextView)
    TextView mPhoneLabel;
    @BindView(R.id.addressTextView)
    TextView mAddressLabel;
    @BindView(R.id.saveParlorButton)
    TextView mSaveRestaurantButton;

    private BeautyParlor mParlor;

    public LibraryDetailFragment() {
        // Required empty public constructor
    }

    public static LibraryDetailFragment newInstance(BeautyParlor parlor) {
        LibraryDetailFragment libraryDetailFragment = new LibraryDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("parlor", Parcels.wrap(parlor));
        libraryDetailFragment.setArguments(args);
        return libraryDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParlor = Parcels.unwrap(getArguments().getParcelable("parlor"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parlor_detail, container, false);
        ButterKnife.bind(this, view);

        Picasso.get().load(mParlor.getImageUrl()).into(mImageLabel);

        mNameLabel.setText(mParlor.getName());

        mRatingLabel.setText(Double.toString(mParlor.getRating()) + "/5");
        mPhoneLabel.setText(mParlor.getPhone());
        mAddressLabel.setText(mParlor.getAddress());
        mWebsiteLabel.setOnClickListener(this);
        mPhoneLabel.setOnClickListener(this);
        mAddressLabel.setOnClickListener(this);
        mSaveRestaurantButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mWebsiteLabel) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mParlor.getWebsite()));
            startActivity(webIntent);
        }
        if (v == mPhoneLabel) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mParlor.getPhone()));
            startActivity(phoneIntent);
        }
        if (v == mAddressLabel) {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:" + mParlor.getLatitude()
                            + "," + mParlor.getLongitude()
                            + "?q=(" + mParlor.getName() + ")"));
            startActivity(mapIntent);
        }
        if (v == mSaveRestaurantButton) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            DatabaseReference parlorRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FIREBASE_CHILD_PARLORS)
                    .child(uid);
            DatabaseReference pushRef = parlorRef.push();
            String pushId = pushRef.getKey();
            mParlor.setPushId(pushId);
            pushRef.setValue(mParlor);
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }

    }
}
