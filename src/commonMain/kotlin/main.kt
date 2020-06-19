import com.soywiz.korge.Korge
import com.soywiz.korge.scene.Module
import com.soywiz.korgw.*
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import gameStateManager.GameDependency
import gameStateManager.scenes.GameOverScene
import gameStateManager.scenes.GameScene
import gameStateManager.scenes.HelpScene
import gameStateManager.scenes.MainMenuScene
import kotlinx.coroutines.*
import kotlin.coroutines.*
import kotlin.native.concurrent.*

suspend fun main() = Korge(Korge.Config(module = GameModule))

@ThreadLocal
lateinit var gameCoroutineContext: CoroutineDispatcher

//Starts the game and manages which scene is rendered
object GameModule : Module() {

    override val title = "Star Battlers: Requiem"
    override val icon = "character/pitrizzo-SpaceShip-gpl3-opengameart-96x96.png"

    override val size = SizeInt(1280, 720) // Virtual Size
    override val windowSize = SizeInt(1280, 720) // Window Size

    override val mainScene = MainMenuScene::class     // The default scene is the main menu
//    override val fullscreen: Boolean = true

    override suspend fun AsyncInjector.configure() { // Allow different scenes to change between each other
        gameCoroutineContext = get<GameWindow>().coroutineDispatcher
        mapInstance(GameDependency("Space Battlers: Requiem"))  // call the first scene Main Menu
        //DONT FORGET TO ADD THE SCENES TO THE MAP PROTOTYPE OR YOU WILL GET AN ERROR ABOUT THE SCENE MANAGER NOT BEING ABLE TO FIND IT
        mapPrototype { MainMenuScene(get()) }
        mapPrototype { GameScene(get()) }
        mapPrototype { GameOverScene() }
        mapPrototype { HelpScene() }
    }

}