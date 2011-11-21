package br.andengine.teste;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;

public class TutorialAndengineActivity extends BaseGameActivity {
	// Essa string vai ser utilizada para filtrar o LogCat
	public static final String TAG = "AndEngineTest";

	private int cameraWidth;
	private int cameraHeight;

	private BitmapTextureAtlas fontTexture;
	private Font font;

	private BitmapTextureAtlas bitmapTextureAtlas;
	private TextureRegion texturaImagem;

	@Override
	public void onLoadComplete() {
		// Esse método é chamado quando tudo é carregado, ou seja, depois que
		// todos os outros são chamadas
		Log.i(TAG, "onLoadComplete()");
	}

	@Override
	public Engine onLoadEngine() {
		// Aqui é preciso configurar um objeto Engine com as configurações do
		// seu app. Este método é chamado primeiro
		Log.i(TAG, "onLoadEngine()");

		Display displayPadrao = getWindowManager().getDefaultDisplay();
		cameraWidth = displayPadrao.getWidth();
		cameraHeight = displayPadrao.getHeight();
		Log.i(TAG, "cameraWidth: " + cameraWidth + " cameraHeight: "
				+ cameraHeight);

		// Esse cara faz com que o app funcione direito em qualquer resolução
		RatioResolutionPolicy resolucao = new RatioResolutionPolicy(
				cameraWidth, cameraHeight);

		Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);

		EngineOptions configuracoesEngine = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE, resolucao, camera);

		return new Engine(configuracoesEngine);
	}

	@Override
	public void onLoadResources() {
		// Aqui você vai carregar os recursos que seu app vai precisas, como
		// imagens, fontes sons e etc.
		Log.i(TAG, "onLoadResources()");

		fontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(fontTexture, Typeface.create(Typeface.DEFAULT,
				Typeface.BOLD), 40, true, Color.BLACK);
		// O atributo mEngine é a engine que você configurou no método
		// onLoanEngine(), que é herdado do BaseGameActivity
		mEngine.getTextureManager().loadTexture(fontTexture);
		mEngine.getFontManager().loadFont(font);

		// Um BitmapTextureAtlas é um bitmap gigante que vai conter dentro dele
		// todos os outros bitmaps que você precisar
		bitmapTextureAtlas = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// Esses dois parametros (128, 128) são o tamanho desse bitmap gigante,
		// e precisam ser uma potência de dois

		// A classe BitmapTextureAtlasTextureRegionFactory possui uma série de
		// métodos estáticos para que você carregue os recursos
		// A AndEngine utiliza como padrão pra recursos externos a pasta assets,
		// e com esse método você seta o caminho padrão para os recursos, no
		// caso vai ser a pasta gfx
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// A textura é apenas a imagem que vai ser carregada para o atlas de
		// bitmap que você criou ali em cima
		texturaImagem = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				bitmapTextureAtlas, this, "icon.png", 0, 0);
		// Esses últimos parametros (0,0) indicam qual a posição desta textura
		// no atlas de bitmap

		mEngine.getTextureManager().loadTexture(bitmapTextureAtlas);

	}

	@Override
	public Scene onLoadScene() {
		// Esse método carrega a cena do jogo, ou seja, a camada onde tudo vai
		// ser desenhado
		Log.i(TAG, "onLoadScene()");

		Scene cenaPrincipal = new Scene();
		cenaPrincipal.setBackground(new ColorBackground(0.1f, 0.3f, 0.8f));

		String helloWorld = "Hello World!\n" + getString(R.string.app_name);
		Text texto = new Text(0, 0, font, helloWorld);

		// A textura que foi carregada lá no método onLoadResource() é utilizada
		// aqui, na hora de criar o sprite
		int imagemX = cameraWidth / 2 - texturaImagem.getWidth() / 2;
		int imagemY = cameraHeight / 2 - texturaImagem.getHeight() / 2;
		Sprite imagem = new Sprite(imagemX, imagemY, texturaImagem);

		cenaPrincipal.attachChild(texto);
		cenaPrincipal.attachChild(imagem);
		return cenaPrincipal;
	}
}