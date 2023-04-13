package com.example.myapp


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.myapp.api.MovieService
import com.example.myapp.api.RetrofitHelper
import com.example.myapp.model.Movie
import com.example.myapp.model.MovieList
import com.example.myapp.repository.MovieRepository
import com.example.myapp.room.MovieDatabase
import com.example.myapp.viewmodel.MainViewModel
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieActivity() : ComponentActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var movieDatabase: MovieDatabase
    lateinit var movieRepository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE movies ADD COLUMN isFavourite INTEGER NOT NULL DEFAULT 0")
                Log.e("INSIDE", "MIGRATION")
            }
        }


        val movieService = RetrofitHelper.getInstance().create(MovieService::class.java)
        movieDatabase = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "movies"
        ).addMigrations(MIGRATION_1_2).build()


        movieRepository = MovieRepository(movieService, movieDatabase, applicationContext)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(movieDatabase, movieRepository)).get(
                MainViewModel::class.java
            )
        var mov: List<MovieList>

        mainViewModel.movies.observe(this@MovieActivity, Observer {
            mov = it.Movie_List
            var list = MovieList("2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2")

            var inr = mov.indexOf(list)

            //Toast.makeText(this@MainActivity, it.Movie_List.toString(), Toast.LENGTH_SHORT)
            //  .show()
            setContent {
                MyAppTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Navigation(mov, inr, this@MovieActivity)
                    }
                }
            }
        })
    }


    @Composable
    fun MovieItem(movie: MovieList, onDeleteMovie: (MovieList) -> Unit, ) {
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        Card(
            modifier = androidx.compose.ui.Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth(), shape = RoundedCornerShape(8.dp), elevation = 4.dp
        ) {
            Surface() {

                Row(
                    androidx.compose.ui.Modifier
                        .padding(4.dp)
                        .fillMaxSize()
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = movie.Movie_Poster,
                            builder = {
                                scale(Scale.FILL)
                                transformations(CircleCropTransformation())
                            }
                            // Placeholder image resource
                        ),
                        contentDescription = movie.Genres,
                        modifier = Modifier
                            .fillMaxHeight().padding(end = 8.dp)
                            .weight(0.2f).size(100.dp)



                    )







                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = androidx.compose.ui.Modifier
                            .padding(4.dp)
                            .fillMaxHeight()
                            .weight(0.8f)
                            .width(200.dp)

                    ) {
                        Text(
                            text = movie.Title.toString(),
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Year: "+movie.Year.toString(),
                            style = MaterialTheme.typography.caption,
                            modifier = androidx.compose.ui.Modifier

                        )
                        Text(
                            text = "Runtime: "+movie.Runtime.toString(),
                            style = MaterialTheme.typography.caption,
                            modifier = androidx.compose.ui.Modifier

                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cast:\n"+movie.Cast.toString(),
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            //FavoriteButton(movie = movie)
                            Button(
                                onClick = {
                                    Toast.makeText(
                                        this@MovieActivity,
                                        "Added to Favourites!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    movie.isFavourite = true

                                    viewModel.viewModelScope.launch {
                                        withContext(Dispatchers.IO) {

                                            var movi = movieDatabase.movieDao().getMovie()
                                            withContext(Dispatchers.Main){
                                                //viewModel.updateMovie(movie)
                                                movieLiveData.postValue(Movie(movi))
                                            }

                                        }
                                    }

                                }
                                ,
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
                            ) {
                                Text(text = "Favourite")
                            }

                            Button(
                                onClick = { onDeleteMovie(movie) },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                            ) {
                                Text(text = "Delete")
                            }
                        }
                    }


                }
            }
        }

    }


    @Composable
    fun Navigation(movieList: List<MovieList>, inr: Int, context: Context) {
        val navController = rememberNavController()

        val items = setOf(
            BottomNavItem("Home", Icons.Filled.Home, "fragment_one"),
            BottomNavItem("Favorites", Icons.Filled.Favorite, "fragment_two"),
            BottomNavItem("user Profile", Icons.Filled.Person, "fragment_three")
        ).toMutableList()

        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    items.forEach { item ->
                        BottomNavigationItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }, content = { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "fragment_one",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("fragment_one") {
                        fragmentOne(movieList, inr, context, onDeleteMovie = { movie ->
                            onDel(movie)

                            // Delete the movie from the database
                        })
                    }
                    // Delete the movie from the database
                    composable("fragment_two") { fragmentTwo(movieList,context) }
                    composable("fragment_three") { fragmentThree() }
                }
            }
        )
    }

    fun onDel(movie: MovieList) {
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        lifecycleScope.launch {
            // Delete the movie from the database on a background thread
            withContext(Dispatchers.IO) {
                viewModel.deleteItem(movie)
                //viewModel.removeItem(movie)
                val mov = movieDatabase.movieDao().getMovie()
                movieLiveData.postValue(Movie(mov))
                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    Log.e("names", mov.toString())
                    /* setContent {
                         Navigation(mov,
                             inr = 6,
                             context = this@MainActivity
                         )
                     }*/
                }
            }
        }
    }

    @Composable
    fun fragmentThree() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "MoviesApp") },
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            },
            content = { innerPadding ->
                val typography = MaterialTheme.typography

                Column(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.user),
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Archita Todi", style = typography.h5)
                    Text(text = "@architatodi1@gmail.com", style = typography.subtitle1)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Number:", style = typography.subtitle1)
                    Text(
                        text = "9830113381",
                        style = typography.body1
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Location:", style = typography.subtitle1)
                    Text(text = "Kolkata, West Bengal", style = typography.subtitle2)

                }


            }
        )
    }

    @Composable
    fun fragmentOne(
        mov: List<MovieList>, inr: Int, context: Context,
        onDeleteMovie: (MovieList) -> Unit

    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "MoviesApp") },
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            },
            content = { innerPadding ->
                var flag = false
                Column(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn {

                        itemsIndexed(items = mov) { index, item ->
                            if (index == inr) {
                                if (flag == false) {
                                    Toast.makeText(
                                        context,
                                        "Second API IS CALLED!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e("Hello", "Second API IS CALLED!")
                                    flag = true
                                }

                            } else {
                                MovieItem(movie = item, onDeleteMovie = onDeleteMovie)

                            }
                        }
                    }
                }
            })
    }

    @Composable
    fun fragmentTwo(mov: List<MovieList>, context: Context) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "MoviesApp") },
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            }, content = { innerPadding ->
                val typography = MaterialTheme.typography

                Column(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val favoriteMovies = mov.filter {
                        it.isFavourite == true
                    }

                    LazyColumn {
                        itemsIndexed(items = favoriteMovies) { index, item ->
                            Card(
                                modifier = androidx.compose.ui.Modifier
                                    .padding(8.dp, 4.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                elevation = 4.dp
                            ) {
                                Surface() {

                                    Row(
                                        androidx.compose.ui.Modifier
                                            .padding(4.dp)
                                            .fillMaxSize()
                                    ) {
                                        Image(
                                            painter = rememberImagePainter(
                                                data = item.Movie_Poster,
                                                builder = {
                                                    scale(Scale.FILL)
                                                    transformations(CircleCropTransformation())
                                                }
                                                // Placeholder image resource
                                            ),
                                            contentDescription = item.Genres,
                                            modifier = Modifier
                                                .fillMaxHeight().padding(end = 8.dp)
                                                .weight(0.2f).size(100.dp)



                                        )


                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            modifier = androidx.compose.ui.Modifier
                                                .padding(4.dp)
                                                .fillMaxHeight()
                                                .weight(0.8f)
                                                .width(200.dp)

                                        ) {
                                            Text(
                                                text = item.Title.toString(),
                                                style = MaterialTheme.typography.subtitle1,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Year: "+item.Year.toString(),
                                                style = MaterialTheme.typography.caption,
                                                modifier = androidx.compose.ui.Modifier

                                            )
                                            Text(
                                                text = "Runtime: "+item.Runtime.toString(),
                                                style = MaterialTheme.typography.caption,
                                                modifier = androidx.compose.ui.Modifier

                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Cast:\n"+item.Cast.toString(),
                                                style = MaterialTheme.typography.body1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))

                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        )

    }

    data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)


    private val movieLiveData = MutableLiveData<Movie>()

    val movies: LiveData<Movie>
        get() = movieLiveData

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }
}

