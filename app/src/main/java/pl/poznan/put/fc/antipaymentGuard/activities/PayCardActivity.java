package pl.poznan.put.fc.antipaymentGuard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.orm.SugarRecord;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.adapters.PayCardSectionAdapter;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;

public class PayCardActivity extends AppCompatActivity {
    private PayCardSectionAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final PayCard payCard = SugarRecord.findById(PayCard.class, getIntent().getExtras().getLong("payCardId"));
        mSectionsPagerAdapter = new PayCardSectionAdapter(getSupportFragmentManager(), this, payCard);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fab.hide();
                        break;
                    case 1:
                        fab.show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayCardActivity.this, AddPayCardTransactionActivity.class);
                intent.putExtra("payCardId", payCard.getId());
                startActivity(intent);
            }
        });

    }
}
