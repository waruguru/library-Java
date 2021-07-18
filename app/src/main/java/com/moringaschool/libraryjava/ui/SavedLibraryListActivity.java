package com.moringaschool.libraryjava.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SavedLibraryListActivity extends AppCompatActivity {
    private DatabaseReference mParlorReference;
    private FirebaseRecyclerAdapter<BeautyParlor , FirebaseParlorViewHolder> mFirebaseAdapter;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    private ParlorListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parlor);
        ButterKnife.bind(this);
        final ArrayList<BeautyParlor> parlors = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_PARLORS).child(uid);
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    parlors.add(snapshot.getValue(BeautyParlor.class));

                }
                mAdapter = new ParlorListAdapter(SavedParlorListActivity.this, parlors);
                mAdapter = new ParlorListAdapter(getApplicationContext(), parlors);
                mRecyclerView.setAdapter(mAdapter);
                RecyclerView.LayoutManager layoutManager =
                        new LinearLayoutManager(SavedParlorListActivity.this);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(true);

                showParlors();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void showParlors() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }
}
