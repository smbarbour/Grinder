package grinder;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ICrafting;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.SlotFurnace;
import net.minecraft.src.TileEntityFurnace;

public class ContainerGrinder extends Container {
	public TileGrinder tileEntity;
	public int progress = 0;
	public int power = 0;
	
	public ContainerGrinder(InventoryPlayer invPlayer, TileGrinder tileGrinder) {
		this.tileEntity = tileGrinder;
		this.addSlot(new Slot(tileGrinder, 0, 56, 35));
		this.addSlot(new SlotFurnace(invPlayer.player, tileGrinder, 1, 116, 35));
		
		int var3;
        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(invPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(invPlayer, var3, 8 + var3 * 18, 142));
        }

	}

	public void updateCraftingResults() {
		super.updateCraftingResults();
		
		for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
			ICrafting var2 = (ICrafting)this.crafters.get(var1);
			if (this.progress != this.tileEntity.progress) {
				var2.updateCraftingInventoryInfo(this, 0, this.tileEntity.progress);
			}
			
			if (this.power != this.tileEntity.power) {
				var2.updateCraftingInventoryInfo(this, 1, this.tileEntity.power & '\uffff');
				var2.updateCraftingInventoryInfo(this, 2, this.tileEntity.power >>> 16);
			}
		}
		
		this.progress = this.tileEntity.progress;
		this.power = this.tileEntity.power;
	}
	
	public void updateProgressBar(int var1, int var2) {
		if(var1 == 0) {
			this.tileEntity.progress = (short)var2;
		}
		if(var1 == 1) {
			this.tileEntity.power = var2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return this.tileEntity.isUseableByPlayer(var1);
	}
	
	@Override
	public ItemStack transferStackInSlot(int par1) {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(par1);
        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 == 1)
            {
                if (!this.mergeItemStack(var4, 2, 38, true))
                {
                    return null;
                }
            }
            else if (par1 != 0)
            {
                if (FurnaceRecipes.smelting().getSmeltingResult(var4.getItem().shiftedIndex) != null)
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (par1 >= 3 && par1 < 30)
                {
                    if (!this.mergeItemStack(var4, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (par1 >= 30 && par1 < 39 && !this.mergeItemStack(var4, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var4, 3, 39, false))
            {
                return null;
            }
            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack)null);
            }
            else
            {
                var3.onSlotChanged();
            }
            if (var4.stackSize == var2.stackSize)
            {
                return null;
            }
            var3.onPickupFromSlot(var4);
        }
        return var2;
	}
}
