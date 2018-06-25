# Xtend

- Apply the content view to Activities, Fragments or XtendFragments using @ContentView.
- Ui binding with the ```@UI``` fields annotation.
- Event binding with ```@Click``` and ```@LongClick``` annotations for methods.
- Apply a PagerAdapter using the ```@SectionPagerAdapter``` annotation.
- ```Fragments``` without inheriting from ```Fragment``` with ```FragmentBuilder``` and working only with annotations.


## Install the library

#### Using gradle

Add the jitpack repository
```gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

And add the dependency and the support for java 8+
```gradle

android {
    ...
    CompileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
    }
}

dependencies {
    ...
    implementation 'com.github.zerh:xtend:master-SNAPSHOT'
}
```

#### Using the jar file
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
Using ```@UI``` bind the field with the xml component, if both have the same name. Also, ```@ContentView``` is responsible for loading the layout in the annotated ```Fragment``` or ```Activity```. In the case of ```@Click``` and ```@LongClick```, the methods must have the same names of the components to which the events will apply, as bellow.

```java
@ContentView(R.layout.activity_main)
public class MyApp extends AppCompatActivity {

    @UI Toolbar myToolbar;
    @UI TextView myTextView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
@ContentView(R.layout.activity_main)
public class MyApp extends AppCompatActivity {

    @UI(R.id.my_toolbar)
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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

##### MyActivity.java
```java
@ContentView(R.layout.activity_main)
public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Xtend.map(this);
    }

    @SectionPagerAdapter(id = R.id.container, count = 3)
    Fragment getItem(int position) {
        SectionScreen sectionScreen = new SectionScreen(position);
        return FragmentBuilder.build(sectionScreen);
    }
}
```

##### SectionScreen.java
```java
@ContentView(R.layout.my_fragment)
public class SectionScreen {

    int sectionNumber;

    @UI TextView myTextView;
    
    public SectionScreen(int sectionNumber){
        this.sectionNumber = sectionNumber;
    }

    @PostInflated
    public void init(Resources resources) {
        String text = resources.getString(R.string.hello_from_section) + sectionNumber;
        myTextView.setText(text);
    }
}

```

##### strings.xml
```xml
<resources>
    ...
    <string name="hello_from_section">"Hello World from section: "</string>
</resources
```

#### Result

<img src="https://raw.githubusercontent.com/zerh/xtend/master/img/App.gif" alt="App" width="350" />



