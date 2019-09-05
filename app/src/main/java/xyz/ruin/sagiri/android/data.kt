package xyz.ruin.sagiri.android

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.ruin.sagiri.booru.ApiClient
import xyz.ruin.sagiri.booru.DanbooruClient
import xyz.ruin.sagiri.booru.IBooruClient
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.RoomDatabase
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel







enum class SagiriServiceOperation {
    POST
}

@Suppress("NOTHING_TO_INLINE")
private inline fun <T : Enum<T>> T.toInt(): Int = this.ordinal

private inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]

class Converters {
    @TypeConverter
    fun fromString(value: String?): Uri? = value?.let { Uri.parse(it) }

    @TypeConverter
    fun uriToString(uri: Uri?): String? = uri?.toString()

    @TypeConverter fun accountTypeToTnt(value: AccountType) = value.toInt()
    @TypeConverter fun intToAccountType(value: Int) = value.toEnum<AccountType>()
}


enum class AccountType {
    DANBOORU
}

@Entity
data class Account(
    @PrimaryKey val name: String,
    val auth: String,
    val username: String,
    val baseUrl: Uri,
    val type: AccountType
) {
    fun makeClient(): IBooruClient =
            when (type) {
                AccountType.DANBOORU -> ApiClient(baseUrl, username, auth).run(::DanbooruClient)
            }
}

@Dao
interface AccountDao {
    @Query("SELECT * FROM account ORDER BY name")
    fun getAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM account WHERE name = :accountId")
    fun getAccount(accountId: String): LiveData<Account>

    @Insert
    fun insert(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(accounts: List<Account>)

    // TODO: remove
    @Query("DELETE FROM account")
    fun deleteAll()
}

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: AccountDao = AppDatabase.getInstance(application.baseContext).accountDao()

    val allAccounts: LiveData<List<Account>>

    init {
        allAccounts = dao.getAccounts()
    }

    fun insert(account: Account) {
        dao.insert(account)
    }
}

@Database(entities = [Account::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao

    private class PopulateDbAsync internal constructor(db: AppDatabase) : AsyncTask<Void, Void, Void>() {
        private val accountDao: AccountDao = db.accountDao()

        override fun doInBackground(vararg params: Void): Void? {
            accountDao.deleteAll()

            val account = Account("test", "Owpt0cA9jy7yvzGWHLwt1_TZ1JpkNaEsq_e5YVZHbV0", "necoma", Uri.parse("https://danbooru.donmai.us"), AccountType.DANBOORU)
            accountDao.insert(account)

            return null
        }
    }

    companion object {
        private const val DATABASE_NAME = "sagiri"

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        private val databaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                PopulateDbAsync(instance!!).execute()
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(databaseCallback)
                .build()
        }
    }
}