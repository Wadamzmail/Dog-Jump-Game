package dev.mutwakil.dogjump.res;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import dev.mutwakil.dogjump.MainActivity;
import dev.mutwakil.dogjump.util.AESUtil;

public class MyAssetManager extends AssetManager {
    public void loadEncrypted(String str, String str2, Class<?> cls) {
        byte[] decrypt = decrypt(Gdx.files.internal(str).readBytes());
        FileHandle local = Gdx.files.local(str2);
        local.writeBytes(decrypt, false);
        load(local.path(), cls);
        finishLoading();
        local.delete();
    }

    private byte[] decrypt(byte[] bArr) {
        try {
            return AESUtil.encrypt(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            MainActivity.toast(e.toString());
            return new byte[0];
        }
    }
}