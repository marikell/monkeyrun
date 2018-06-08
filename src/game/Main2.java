/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import java.util.LinkedList;
import models.Element;
import models.Game;
import models.RigidBodyBox;

/**
 *
 * @author Marianne
 */
public class Main2 extends SimpleApplication implements ActionListener, PhysicsCollisionListener {

    private BulletAppState bulletAppState;
    private Game game;

    private RigidBodyBox floorBox;
    private Element element;

    private AnimChannel channel;
    private AnimControl control;
    private Node player;

    public static void main(String[] args) {
        Main2 app = new Main2();
        app.showSettings = false;
        app.start();
    }

    public void initGame() { 
        createBackground();        
        initKeys();
        //Configurações da Câmera
        cam.getLocation().z += 33f;
        cam.getLocation().y += 2f;
        flyCam.setEnabled(false);
        //Inicialização do BulletAppState
        initBulletAppState();
        createLight(ColorRGBA.White);
        bulletAppState.setDebugEnabled(true);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        //Instância de um jogo
        game = new Game();
        game.createInitialScreen(assetManager, guiNode, guiFont);
    }

    public void createBackground(){
        Box backMesh = new Box(35f, 35f, 1f);
        Vector2f scale = new Vector2f(5,5);        
        backMesh.scaleTextureCoordinates(scale);
        Geometry backGeo = new Geometry("Box", backMesh);
        Material backMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture backTex = assetManager.loadTexture("Textures/background.jpg");
        backTex.setWrap(WrapMode.Repeat);
        backMat.setTexture("ColorMap", backTex);
        backGeo.setMaterial(backMat);
        backGeo.rotate(0.6f, 0, 0);
        backGeo.move(0, 0, 15);
        System.out.println(backGeo.getLocalTranslation());
        rootNode.attachChild(backGeo);
    }
    
    private void drawElements() {
        LinkedList<Element> elements = (LinkedList<Element>) game.getScene().getElements();

        for (int i = 0; i < elements.size(); i++) {

            Spatial element = elements.get(i).getBox();

            rootNode.attachChild(elements.get(i).getBox());
        }

        //Desenhando o player        
        rootNode.attachChild(game.getPlayer().getNode());

    }

    public void initBulletAppState() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
    }

    private DirectionalLight createLight(ColorRGBA color) {
        viewPort.setBackgroundColor(color);
        DirectionalLight light = new DirectionalLight();
        light.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        rootNode.addLight(light);
        return light;
    }

    @Override
    public void simpleInitApp() {

        initGame();
        rootNode.rotate(2.5f, 0, 0);

    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

    }

    @Override
    public void collision(PhysicsCollisionEvent event) {

    }

    private void initKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "Space", "Left", "Right");
    }

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Space") && !game.getStatus()) {                
                game.setStatus(true);
                game.createScene(assetManager, bulletAppState);
                guiNode.detachAllChildren();

                //Instância do player
                game.createPlayer(assetManager);
                drawElements();
            }
            if (name.equals("Left") && keyPressed) {
                Node player = (Node) rootNode.getChild("monkey");
                if (player.getLocalTranslation().x > -1) {
                    player.move(-3f, 0, 0);
                }
            }
            if (name.equals("Right") && keyPressed) {
                Node player = (Node) rootNode.getChild("monkey");
                if (player.getLocalTranslation().x < 1) {
                    player.move(3f, 0, 0);
                }
            }
        }
    };

}
