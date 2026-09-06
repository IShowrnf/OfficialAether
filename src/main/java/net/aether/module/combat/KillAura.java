package net.aether.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;
import net.aether.setting.ModeSetting;
import net.aether.setting.NumberSetting;
import net.aether.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class KillAura extends Module
{
    public final NumberSetting range = new NumberSetting("Range", 4.5D, 1.0D, 6.0D, 0.1D);
    public final NumberSetting aps = new NumberSetting("APS", 12.0D, 1.0D, 20.0D, 0.5D);
    public final NumberSetting maxTargets = new NumberSetting("Max Targets", 3.0D, 1.0D, 10.0D, 1.0D);
    public final ModeSetting rotation = new ModeSetting("Rotation", "Smooth", "None", "Snap", "Smooth");
    public final ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Health", "Angle");
    public final BooleanSetting autoBlock = new BooleanSetting("Auto Block", true);
    public final BooleanSetting throughWalls = new BooleanSetting("Through Walls", false);
    public final BooleanSetting players = new BooleanSetting("Players", true);
    public final BooleanSetting mobs = new BooleanSetting("Mobs", false);
    public final ModeSetting rSwitch = new ModeSetting("R-Switch", "Off", "Off", "Sequential", "Random");

    private long lastAttack;

    public KillAura()
    {
        super("KillAura", "Automatically attacks entities.", Category.COMBAT);
        this.addSettings(this.range, this.aps, this.maxTargets, this.rotation, this.priority, this.autoBlock, this.throughWalls, this.players, this.mobs);
        this.addSettings(this.rSwitch.group("R-Switch"));
    }

    public void onTick()
    {
        if (!this.inGame())
        {
            return;
        }

        long interval = (long)(1000.0D / this.aps.getValue());

        if (System.currentTimeMillis() - this.lastAttack < interval)
        {
            return;
        }

        List<EntityLivingBase> targets = this.findTargets();

        if (targets.isEmpty())
        {
            return;
        }

        this.lastAttack = System.currentTimeMillis();

        for (EntityLivingBase target : targets)
        {
            if (!this.rotation.is("None"))
            {
                EntityUtil.faceEntity(target, this.rotation.is("Smooth"));
            }

            mc.playerController.attackEntity(this.player(), target);
            this.player().swingArm(EnumHand.MAIN_HAND);
        }
    }

    private List<EntityLivingBase> findTargets()
    {
        List<EntityLivingBase> found = new ArrayList<EntityLivingBase>();
        double maxRange = this.range.getValue();

        for (Entity entity : this.world().loadedEntityList)
        {
            if (!(entity instanceof EntityLivingBase) || entity == this.player())
            {
                continue;
            }

            EntityLivingBase living = (EntityLivingBase)entity;

            if (!living.isEntityAlive() || living.getHealth() <= 0.0F)
            {
                continue;
            }

            if (living instanceof EntityPlayer && !this.players.getValue())
            {
                continue;
            }

            if (living instanceof IMob && !this.mobs.getValue())
            {
                continue;
            }

            if (!(living instanceof EntityPlayer) && !(living instanceof IMob))
            {
                continue;
            }

            if (this.player().getDistanceToEntity(living) > maxRange)
            {
                continue;
            }

            if (!this.throughWalls.getValue() && !this.player().canEntityBeSeen(living))
            {
                continue;
            }

            found.add(living);
        }

        found.sort(this.getComparator());

        while (found.size() > this.maxTargets.getInt())
        {
            found.remove(found.size() - 1);
        }

        return found;
    }

    private Comparator<EntityLivingBase> getComparator()
    {
        if (this.priority.is("Health"))
        {
            return new Comparator<EntityLivingBase>()
            {
                public int compare(EntityLivingBase a, EntityLivingBase b)
                {
                    return Float.compare(a.getHealth(), b.getHealth());
                }
            };
        }

        if (this.priority.is("Angle"))
        {
            return new Comparator<EntityLivingBase>()
            {
                public int compare(EntityLivingBase a, EntityLivingBase b)
                {
                    return Float.compare(EntityUtil.getAngleTo(a), EntityUtil.getAngleTo(b));
                }
            };
        }

        return new Comparator<EntityLivingBase>()
        {
            public int compare(EntityLivingBase a, EntityLivingBase b)
            {
                return Float.compare(mc.player.getDistanceToEntity(a), mc.player.getDistanceToEntity(b));
            }
        };
    }
}
