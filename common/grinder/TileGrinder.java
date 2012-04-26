package grinder;

import java.util.Random;

import buildcraft.api.IPowerReceptor;
import buildcraft.api.Orientations;
import buildcraft.api.PowerFramework;
import buildcraft.api.PowerProvider;
import buildcraft.core.Utils;

import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.ISidedInventory;
import net.minecraft.src.forge.MinecraftForge;

public class TileGrinder extends TileEntity implements ISidedInventory, IPowerReceptor {
	public ItemStack[] inventory;
	PowerProvider powerProvider;
	public short progress;
	public int power;
	private static int maxPower = 512;
	private static int maxProgress = 100;
	
	public TileGrinder () {
		this.inventory = new ItemStack[2];
		if (PowerFramework.currentFramework != null) { 
			this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
		}
		this.powerProvider.configure(50, 1, 25, 5, 100);
	}

	public Container getGuiContainer(InventoryPlayer invPlayer) {
		return new ContainerGrinder(invPlayer, this);
	}

	@Override
	public void setPowerProvider(PowerProvider provider) {
		powerProvider = provider;
	}

	@Override
	public PowerProvider getPowerProvider() {
		return powerProvider;
	}
	
	@Override
	public void doWork() {
		if (this.power < this.maxPower) {
			int receivedPower = this.powerProvider.useEnergy(1,25, true);
			this.power += receivedPower;
			if (this.power > this.maxPower) {
				this.power = this.maxPower;
			}
		}
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		this.powerProvider.update(this);
		boolean isPowered = this.power > 0;
		boolean inventoryChanged = false;
		if (!this.worldObj.isRemote) {
			
			if (isPowered && hasWork()){
				float overdriveRating = (float)this.power/(float)this.maxPower;
				if(overdriveRating > 0.8) {
					this.power -= 8;
					this.progress += 8;
				} else if (overdriveRating > 0.5) {
					this.power -= 4;
					this.progress += 4;
				} else {
					this.power--;
					this.progress++;
				}
				
				if (this.progress >= this.maxProgress) {
					this.progress = 0;
					completeWork();
					inventoryChanged = true;
				}
			} else if (!hasWork()) {
				this.progress= 0;
			}
		}
		
		if (inventoryChanged) {
			onInventoryChanged();
		}
	}

	private void completeWork() {
		--this.inventory[0].stackSize;
		if (this.inventory[0].stackSize == 0) {
			this.inventory[0] = null;
		}
		Random magic = new Random();
		int result = magic.nextInt(1000);
		ItemStack tempStack;
		if (result > 800) {
			tempStack = new ItemStack(Block.dirt, 1);
		} else if (result > 600) {
			tempStack = new ItemStack(Block.sand, 1);
		} else if (result > 400) {
			tempStack = new ItemStack(Block.gravel, 1);
		} else if (result > 250) {
			tempStack = new ItemStack(Item.coal, 1);
		} else if (result > 150) {
			tempStack = new ItemStack(Item.ingotIron, 1);
		} else if (result > 50) {
			tempStack = new ItemStack(Item.ingotGold, 1);
		} else if (result > 5) {
			tempStack = new ItemStack(Item.dyePowder,1,4);
		} else {
			tempStack = new ItemStack(Item.diamond, 1);
		}
		MinecraftForge.getOreClass("");
		if (Utils.addToRandomPipeEntry(this, Orientations.YNeg, tempStack)) {
			return;
		} else {
			this.inventory[1] = tempStack;
			return;
		}
	}

	@Override
	public int powerRequest() {
		if (this.power < this.maxPower) {
			return getPowerProvider().maxEnergyReceived;
		} else {
			return 0;
		}
	}

	private boolean hasWork() {
		if (inventory[0] != null && inventory[0].getItem().shiftedIndex == Block.cobblestone.blockID && inventory[1] == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return this.inventory[var1];
	}

	@Override
	public ItemStack decrStackSize(int slot, int requestedStackSize) {
		if (this.inventory[slot] != null) {
			ItemStack tempStack;
			if (this.inventory[slot].stackSize <= requestedStackSize) {
				tempStack = this.inventory[slot];
				this.inventory[slot] = null;
				return tempStack;
			} else {
				tempStack = this.inventory[slot].splitStack(requestedStackSize);
				if (this.inventory[slot].stackSize == 0) {
					this.inventory[slot] = null;
				}
				return tempStack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack tempStack) {
		this.inventory[slot] = tempStack;
	}

	@Override
	public String getInvName() {
		return "inventory.grinder";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityPlayer.getDistance((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public int getStartInventorySide(int side) {
		switch (side) {
		case 0:
			return 1;
		case 1:
			return 0;
		default:
			return 1;
		}
	}

	@Override
	public int getSizeInventorySide(int side) {
		return 1;
	}

	public int gaugePowerScaled(int i) {
		return i * this.power / maxPower;
	}

	public int gaugeProgressScaled(int i) {
		return i * this.progress / maxProgress;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		PowerFramework.currentFramework.loadPowerProvider(this, par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.inventory = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.inventory.length)
            {
                this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
            
            this.power = par1NBTTagCompound.getInteger("CurrentPower");
            this.progress = par1NBTTagCompound.getShort("Progress");
            
        }
	}
	
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		PowerFramework.currentFramework.savePowerProvider(this, par1NBTTagCompound);
        par1NBTTagCompound.setInteger("CurrentPower", this.power);
        par1NBTTagCompound.setShort("Progress", this.progress);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.inventory.length; ++var3)
        {
            if (this.inventory[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.inventory[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
	}

}
