# Xtend

- Apply the content view to Activities, Fragments or XtendFragments using @ContentView

- Ui binding with the ```@UI``` fields annotation.

- Event binding with ```@Click``` and ```@LongClick``` annotations for methods

- Apply a PagerAdapter using the ```@SectionPagerAdapter``` annotation

- Fragment subclass ```XtendFragment``` to works with annotations


```java
@ContentView(R.layout.activity_fragment_test)
public class MyApp extends AppCompatActivity {

    @UI Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Xtend.map(this);

        setSupportActionBar(toolbar);
    }

    @Click
    void fab(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @SectionPagerAdapter(id = R.id.container, count = 3)
    Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position);

        PlaceholderFragment fr = new PlaceholderFragment();
        fr.setArguments(args);
        return fr;
    }

    @ContentView(R.layout.fragment_fragment_test)
    public static class PlaceholderFragment extends XtendFragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        @UI
        public TextView myTextView;

        @PostInflated
        public void init() {
            String text = getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER));
            myTextView.setText(text);
        }
    }

}
```

# Result

<img src="https://raw.githubusercontent.com/zerh/xtend/master/App.gif" alt="App" width="350" />

# Download
Add the jitpack repo
```gradle
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependecy
```gradle
dependencies {
    implementation 'com.github.zerh:xtend:master-SNAPSHOT'
}
```
Set the java 8 compilation in the build.gradle(Module: app) file

```gradle

android {
    ...
    compileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
    }
}

```
