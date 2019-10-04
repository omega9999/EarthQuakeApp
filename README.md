# EarthQuakeApp
Esercizi con app EarthQuakeApp
https://classroom.udacity.com/courses/ud843

Rif GIT:
https://github.com/udacity/ud843_Soonami
https://github.com/udacity/ud843_DidYouFeelIt
https://github.com/googlesamples/android-NetworkConnect
https://github.com/udacity/ud843-QuakeReport

Casi risolti:
1) Gson generato con (vedere http://www.jsonschema2pojo.org/): attenzione che i numeri non li gestisce bene (int/long/double)
2) shape drawables
    vedi https://developer.android.com/guide/topics/resources/drawable-resource.html#Shape
    e    https://guides.codepath.com/android/Drawables
3) common intent https://developer.android.com/guide/components/intents-common.html
4) permessi sul manifest: 
    https://developer.android.com/guide/topics/permissions/overview
    https://developer.android.com/reference/android/Manifest.permission.html
5) multithread:
    https://developer.android.com/guide/components/processes-and-threads.html
    https://developer.android.com/training/articles/perf-anr.html
6) Android Performance Patterns (multithread):
    https://www.youtube.com/watch?v=qk5F6Bxqhr4&index=1&list=PLWz5rJ2EKKc9CBxr3BVjPTPoDPLdPIFCE
    https://www.youtube.com/watch?v=s4eAtMHU5gI&index=8&list=PLWz5rJ2EKKc9CBxr3BVjPTPoDPLdPIFCE
    https://www.youtube.com/playlist?list=PLWz5rJ2EKKc9CBxr3BVjPTPoDPLdPIFCE
7) uso di AsyncTask
8) uso di Loader (inizialmente quelli deprecati -> quelli androidx): attenzione che non sono accessibili nei Service https://developer.android.com/reference/android/app/Service
    (deprecato) https://developer.android.com/guide/components/loaders.html
    (deprecato) https://developer.android.com/reference/android/app/LoaderManager.html
    (deprecato) https://developer.android.com/reference/android/app/LoaderManager.LoaderCallbacks.html
    (deprecato) esempio: https://www.concretepage.com/android/android-asynctaskloader-example-with-listview-and-baseadapter
9) Liste vuote: 
    https://developer.android.com/reference/android/widget/AdapterView.html#setEmptyView(android.view.View)
    https://material.google.com/patterns/empty-states.html
    https://material.io/design/communication/empty-states.html
10) Check connessione a internet:
    https://developer.android.com/training/basics/network-ops/connecting.html
    https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
11) utilizzo di SharedPreferences e option menu': 
    https://developer.android.com/reference/android/content/SharedPreferences.html
    https://developer.android.com/guide/topics/data/data-storage.html#pref
    https://developer.android.com/guide/topics/ui/menus.html
    (deprecato) https://developer.android.com/reference/android/preference/PreferenceFragment.html
    (deprecato) https://developer.android.com/reference/android/preference/Preference.html
    (deprecato) https://developer.android.com/reference/android/preference/ListPreference.html
12)  Uri.Builder class: https://developer.android.com/reference/android/net/Uri.Builder.html
13) estensione di RecyclerView con EmptyRecyclerView per gestire la lista vuota
14) setOnItemClickListener sull'adapter per gestire il listener mancante sulle righe della RecyclerView
15) lifecycle app dal create -> stop con androidx + destroy con workaround usando un service

TODO:
0) https://alexzh.com/2015/06/26/support-multiple-themes-in-android-application/
1) sostituire i vari layout con le ConstraintLayout (vedere https://github.com/chrisbanes/cheesesquare)
2) usare le https://developer.android.com/guide/topics/ui/layout/recyclerview.html
OK 3) leggere il JSON usando la classe Gson di google (vedere http://www.jsonschema2pojo.org/)
3.1) usare moshi al posto di Gson (occupa meno spazio ed è integrabile con retrofit)
4) shrink code: https://developer.android.com/studio/build/shrink-code
5) provare la classe OkHttpClient https://github.com/square/okhttp
6) provare la classe Retrofit https://square.github.io/retrofit/
7) usare altro al posto dei deprecati PreferenceFragment, Preference, ListPreference
8) per i dettagli inserire scelta tra apertura pagina esterna o activity interna (webview)
9) per le coordinate inserire scelta tra apertura maps esterno o activity interna
10) migliorare l'url di richiesta informazioni
11) vedere se puo' essere utile: 
	https://developers.google.com/maps/documentation/javascript/examples/layer-data-quakes
	https://www.programmableweb.com/category/earthquakes/api
12)  ViewModels + LiveData: https://developer.android.com/reference/androidx/lifecycle/ViewModel +  https://developer.android.com/reference/androidx/lifecycle/LiveData
     MediatorLiveData https://developer.android.com/reference/androidx/lifecycle/MediatorLiveData
     Room database https://developer.android.com/topic/libraries/architecture/room.html
     LiveData guide: https://developer.android.com/topic/libraries/architecture/livedata.html
     ViewModel guide: https://developer.android.com/topic/libraries/architecture/viewmodel.html
13) Preferences nuove
    https://developer.android.com/reference/androidx/preference/package-summary.html
    https://developer.android.com/guide/topics/ui/settings.html
    https://developer.android.com/guide/practices/screens_support
    https://developer.android.com/training/basics/fragments/fragment-ui.html

Altri link utili:
http://www.json.org/
https://www.programmableweb.com/apis/directory
https://developers.google.com/apis-explorer/#p/
https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1
https://developers.google.com/books/docs/v1/getting_started#intro
https://www.data.gov/
https://www.restapitutorial.com/lessons/httpmethods.html
https://developer.android.com/reference/java/net/HttpURLConnection.html
https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
https://developer.android.com/training/basics/network-ops/connecting.html
https://material.io/design/components/progress-indicators.html
https://developer.android.com/reference/android/os/AsyncTask.html
api for "The Guardian" https://open-platform.theguardian.com/documentation/
http://content.guardianapis.com/search?q=debates&api-key=test
http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test

Progetti come esercizio:
- Book Listing: https://review.udacity.com/#!/rubrics/164/view (https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1)
- News App: https://review.udacity.com/#!/rubrics/165/view (http://content.guardianapis.com/search?q=debates&api-key=test)
