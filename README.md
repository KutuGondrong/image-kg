# ImageKg

ImageKg primary focus is on making scrolling any kind of a list of images as smooth 
and fast as possible for display a remote image.

## Usage

Add it in your root build.gradle at the end of repositories:

```

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

```

Step 2. Add the dependency

```

	dependencies {
	        implementation 'com.github.KutuGondrong:image-kg:0.0.1'
	}

```
## Introduction

```
    ImageKG.dslImageHelper {
        context = itemView.context
        urlImage = item.user.image.medium
        this.imageView = imageView
    }.process()
```

```
    val intent = Intent(this, ZoomImageKGViewShowFromURL::class.java)
    intent.putExtra(ZoomImageKGViewShowFromURL.URL_IMAGE, "urlImage")
    startActivity(intent)
```

## Website
[kutugondrong.com](https://kutugondrong.com/)
