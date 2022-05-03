package edu.uidaho.electricblocks.lib;

import mcp.mobius.waila.api.IRegistrar;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public abstract class Feature {
    
    public abstract void initialize (IRegistrar hwyla);
    
    public void addInfo (Collection<ITextComponent> info, String key, Object... args) {
        
        info.add(this.getInfoComponent(key, args));
    }
    
    public ITextComponent getInfoComponent (String key, Object... args) {
        
        return new TranslationTextComponent("info.electricblocks." + key, args);
    }
}