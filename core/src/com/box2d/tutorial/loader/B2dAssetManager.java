package com.box2d.tutorial.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class B2dAssetManager {

    public final AssetManager manager = new AssetManager();

    // Textures
    public final String playerImage = "images/player.png";
    public final String enemyImage = "images/enemy.png";

    //Sounds
    public final String boingSound = "sounds/boing.wav";
    public final String pingSound = "sounds/ping.wav";

    // Music
    public final String playingSong = "music/Rolemusic_-_pl4y1ng.mp3";

    // Skin
    public final String skin = "skin/glassy-ui.json";

    public void queueAddImages() {
        manager.load(playerImage, Texture.class);
        manager.load(enemyImage, Texture.class);
    }

    public void queueAddSounds() {
        manager.load(boingSound, Sound.class);
        manager.load(pingSound, Sound.class);
    }

    public void queueAddMusic() {
        manager.load(playingSong, Music.class);
    }

    public void queueAddSkin() {
        SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
        manager.load(skin, Skin.class, params);
    }
}
