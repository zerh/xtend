# Xtend

- Apply the content view to Activities, Fragments or XtendFragments using @ContentView.
- Ui binding with the ```@UI``` fields annotation.
- Event binding with ```@Click``` and ```@LongClick``` annotations for methods.
- Apply a PagerAdapter using the ```@SectionPagerAdapter``` annotation.
- Fragment subclass ```XtendFragment``` to works with annotations.


## Download
Download the jar file [xtend-0.1.1.jar](https://github.com/zerh/xtend/raw/master/target/xtend-0.1.1.jar) and copy it to the lib folder.

<img src="https://raw.githubusercontent.com/zerh/xtend/master/img/lib-folder.png" alt="lib-folder" width="280" />

And add support for java 8+
```gradle
android {
    ...
    CompileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
    }
}
```

## Getting Started
Using ```@UI``` bind the field with the xml component, if both have the same name. In the case of ```@Click``` and ```@LongClick```, the methods must have the same names of the components to which the events will apply, as bellow.

```java
@ContentView(R.layout.activity_fragment_test)
public class MyApp extends AppCompatActivity {

    @UI Toolbar myToolbar;
    @UI TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Xtend.map(this);

        setSupportActionBar(myToolbar);
    }

    @Click
    void fab(View view) {
        myTextView.setText("Hello World");
    }
}
```

In the case that the components don't have their id written in camel case, the id can be passed to the annotation by parameter:

```java
@ContentView(R.layout.activity_fragment_test)
public class MyApp extends AppCompatActivity {

    @UI(R.id.my_toolbar)
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Xtend.map(this);

        setSupportActionBar(toolbar);
    }

    @Click(R.id.my_textview)
    void fab(View view) {
        myTextView.setText("Hello World");
    }
}
```

## @SectionPagerAdapter

In the ViewPager case, the ```@SectionPagerAdapter``` annotation abstracts a ```FragmentStatePagerAdapter``` using a simple method. You can also use ```FragmentBuilder```, to generate a fragment from a class, in this way you can create fragments without sacrificing their inheritance.

```java
@SectionPagerAdapter(id = R.id.container, count = 3)
Fragment getItem(int position) {

    Bundle args = new Bundle();
    args.putInt(SectionScreen.ARG_SECTION_NUMBER, position);

    return FragmentBuilder.build(SectionScreen.class, args);
}
```

```java
@ContentView(R.layout.fragment_fragment_test)
public static class SectionScreen {

    private static final String ARG_SECTION_NUMBER = "section_number";

    @UI TextView myTextView;

    @PostInflated
    public void init(Bundle bundle, Resources resources) {
        String text = resources.getString(R.string.section_format, bundle.getInt(ARG_SECTION_NUMBER));
        myTextView.setText(text);
    }
}

```

### Result

<img src="https://raw.githubusercontent.com/zerh/xtend/master/img/App.gif" alt="App" width="350" />



