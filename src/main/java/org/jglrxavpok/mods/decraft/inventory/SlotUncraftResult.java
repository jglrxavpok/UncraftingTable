package org.jglrxavpok.mods.decraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUncraftResult extends Slot 
{

	public SlotUncraftResult(IInventory inventoryIn, int index, int xDisplayPosition, int yDisplayPosition) 
	{
		super(inventoryIn, index, xDisplayPosition, yDisplayPosition);
	}

    /**
     * Fired when the player removes the item from the slot.
     */
	@Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
    {
    	super.onPickupFromSlot(playerIn, stack);
    }
	
    /**
     * Check if the stack is a valid item for this slot.
     */
	@Override
    public boolean isItemValid(ItemStack stack)
    {
		return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
    }
	
}
