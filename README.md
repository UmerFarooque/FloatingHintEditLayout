# FloatingHintEditLayout
[![Release](https://jitpack.io/v/UmerFarooque/FloatingHintEditLayout.svg)](https://jitpack.io/#UmerFarooque/FloatingHintEditLayout)

</br>

Custom EditText layout with an error TextView and floating hint TextView. The layout can have only one child EditText.

</br>

![demo-gif](https://i.imgur.com/zEn8lsL.gif)

</br>
</br>

### To add this to your project:

</br>

<b>Step 1:</b> Add JitPack repository to build.gradle file

```
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
}
```

</br>

<b>Step 2:</b> Add the dependency

```
dependencies {
    implementation 'com.github.UmerFarooque:FloatingHintEditLayout:0.1.0'
}
```

</br>

### Methods:

</br>

<b>setErrorText():</b> Set the error message

<b>showError():</b> Show the error message

</br>

### Example:

```
<com.umerfarooque.floatinghinteditlayout.FloatingHintEditLayout
        android:id="@+id/fhel"
        android:layout_margin="16dp"
        android:layout_width="match_parent"
        android:layout_height="92dp"

        app:fhelHintText="Enter Number"
        app:fhelHintActiveColor="@android:color/white"
        app:fhelHintInactiveColor="#7F2EA5"
        app:fhelHintErrorColor="@android:color/holo_red_dark"

        app:fhelErrorText="1 not allowed"
        app:fhelErrorTextColor="@android:color/holo_red_dark"

        app:fhelStrokeActiveColor="@android:color/holo_green_light"
        app:fhelStrokeInactiveColor="#FFA726"
        app:fhelStrokeErrorColor="@android:color/holo_red_dark"

        app:fhelCornerRadius="30dp">
        
        <EditText
            android:id="@+id/test_et"
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:paddingStart="24dp"
            android:paddingEnd="24dp"
            android:inputType="phone"
            android:textSize="16sp" />
            
    </com.umerfarooque.floatinghinteditlayout.FloatingHintEditLayout>
```

</br>
</br>
