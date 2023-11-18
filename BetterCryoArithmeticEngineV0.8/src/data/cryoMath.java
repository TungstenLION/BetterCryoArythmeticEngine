package data;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.InstallableIndustryItemPlugin;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseInstallableItemEffect;
import com.fs.starfarer.api.impl.campaign.econ.impl.ItemEffectsRepo;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;


import static com.fs.starfarer.api.impl.campaign.econ.impl.ItemEffectsRepo.ITEM_EFFECTS;


public class cryoMath extends BaseModPlugin {

    @Override
    public void onApplicationLoad() {

        ITEM_EFFECTS.put("cryoarithmetic_engine", new BaseInstallableItemEffect("cryoarithmetic_engine") {
            public void apply(Industry industry) {
                float bonus = 0.0F;
                if (industry.getMarket().hasCondition("hot")) {
                    bonus = ItemEffectsRepo.CRYOARITHMETIC_FLEET_SIZE_BONUS_HOT;
                    industry.getMarket().addCondition("TL_cryoMathHazHot");
                    industry.getMarket().removeCondition("hot");
                } else if (industry.getMarket().hasCondition("very_hot")) {
                    bonus = ItemEffectsRepo.CRYOARITHMETIC_FLEET_SIZE_BONUS_VERY_HOT;
                    industry.getMarket().addCondition("TL_cryoMathHazVeryHot");
                    industry.getMarket().removeCondition("very_hot");
                }
                industry.getMarket().getStats().getDynamic().getMod("combat_fleet_size_mult").modifyFlat(this.spec.getId(), bonus, Misc.ucFirst(this.spec.getName().toLowerCase()));
            }

            public void unapply(Industry industry) {

                if (industry.getMarket().hasCondition("TL_cryoMathHazHot")) {
                    industry.getMarket().addCondition("hot");
                    industry.getMarket().removeCondition("TL_cryoMathHazHot");
                } else if (industry.getMarket().hasCondition("TL_cryoMathHazVeryHot")) {
                    industry.getMarket().addCondition("very_hot");
                    industry.getMarket().removeCondition("TL_cryoMathHazVeryHot");
                }
                industry.getMarket().getStats().getDynamic().getMod("combat_fleet_size_mult").unmodifyFlat(this.spec.getId());
            }

            protected void addItemDescriptionImpl(Industry industry, TooltipMakerAPI text, SpecialItemData data, InstallableIndustryItemPlugin.InstallableItemDescriptionMode mode, String pre, float pad) {
                text.addPara(pre + "Increases size of fleets launched by colony by %s for hot worlds, and " + "by %s for worlds with extreme heat.", pad, Misc.getHighlightColor(), new String[]{Math.round(ItemEffectsRepo.CRYOARITHMETIC_FLEET_SIZE_BONUS_HOT * 100.0F) + "%", Math.round(ItemEffectsRepo.CRYOARITHMETIC_FLEET_SIZE_BONUS_VERY_HOT * 100.0F) + "%"});
            }

            public String[] getSimpleReqs(Industry industry) {
                return new String[]{ItemEffectsRepo.HOT_OR_EXTREME_HEAT};
            }
        });

    }
}