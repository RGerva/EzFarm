/**
 * Generic Class: ModSoundsProvider <T>
 * A generic structure that works with type parameters.
 *
 * <p>Created by: superuser
 * <p>On: 2026/jun.
 *
 * <p>GitHub: https://github.com/RGerva
 *
 * <p>Copyright (c) 2026 @RGerva. All Rights Reserved.
 *
 * <p>Licensed under the GNU General Public License, Version 3.0.
 */

package com.rgerva.ezfarm.datagen;

import com.rgerva.ezfarm.EzFarm;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundsProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     */
    protected ModSoundsProvider(PackOutput output) {
        super(output, EzFarm.MOD_ID);
    }

    @Override
    public void registerSounds() {

    }
}
