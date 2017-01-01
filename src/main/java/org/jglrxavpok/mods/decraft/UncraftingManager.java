package org.jglrxavpok.mods.decraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;
import org.jglrxavpok.mods.decraft.RecipeHandlers.*;
import org.jglrxavpok.mods.decraft.common.config.ModConfiguration;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipesMapExtending;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;


/**
 * @author jglrxavpok
 *
 */
public class UncraftingManager 
{
    
	private static Boolean canUncraftItem(ItemStack itemStack)
	{
		String uniqueIdentifier = GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).toString();
		if (itemStack.getItemDamage() > 0) uniqueIdentifier += "," + Integer.toString(itemStack.getItemDamage()); 
		
		return ArrayUtils.indexOf(ModConfiguration.excludedItems, uniqueIdentifier) < 0;
	}
	
	
	public static List<Integer> getStackSizeNeeded(ItemStack item)
	{
		List<Integer> list = new ArrayList<Integer>();
		if (!canUncraftItem(item)) return list;
		
		List<IRecipe> recipeList = Lists.newArrayList(Iterables.filter(CraftingManager.getInstance().getRecipeList(), IRecipe.class)); 
		for ( IRecipe recipe : recipeList )
		{
			ItemStack recipeOutput = recipe.getRecipeOutput();
			if (recipeOutput != null)
			{
				if (ItemStackHelper.areItemsEqualIgnoreDurability(item, recipeOutput))
				{
					RecipeHandler handler = getRecipeHandler(recipe);
					if (handler != null)
					{
						list.add(recipeOutput.stackSize);
						break;
					}
					else 
					{
						ModUncrafting.instance.getLogger().error("[Uncrafting Table] Unknown recipe type: " + recipe.getClass().getCanonicalName());
					}
				}
			}
		}
		
		return list;
	}
	
	
	
	
	public static List<ItemStack[]> getUncraftResults(ItemStack item)
	{
		List<ItemStack[]> list = new ArrayList<ItemStack[]>();
		if (!canUncraftItem(item)) return list;
		
		List<IRecipe> recipeList = Lists.newArrayList(Iterables.filter(CraftingManager.getInstance().getRecipeList(), IRecipe.class)); 
		for ( IRecipe recipe : recipeList )
		{
			ItemStack recipeOutput = recipe.getRecipeOutput();
			if (ItemStackHelper.areItemsEqualIgnoreDurability(item, recipeOutput) && recipeOutput.stackSize <= item.stackSize)
			{
				RecipeHandler handler = getRecipeHandler(recipe);
				if (handler != null)
				{
					list.add(handler.getCraftingGrid(recipe));
					break;
				}
				else
				{
					ModUncrafting.instance.getLogger().error("[Uncrafting Table] Unknown recipe type: " + recipe.getClass().getCanonicalName());
				}
			}
		}
		
		return list;
	}
	
	
	private static RecipeHandler getRecipeHandler(IRecipe recipe)
	{
		if (recipe instanceof RecipesMapExtending) return null;
		if (recipe instanceof ShapelessRecipes) return new ShapelessRecipeHandler(ShapelessRecipes.class);
		if (recipe instanceof ShapedRecipes) return new ShapedRecipeHandler(ShapedRecipes.class);
		if (recipe instanceof ShapelessOreRecipe) return new ShapelessOreRecipeHandler(ShapelessOreRecipe.class);
		if (recipe instanceof ShapedOreRecipe) return new ShapedOreRecipeHandler(ShapedOreRecipe.class);
		
		try
		{
			
			Class c;
			
			c = Class.forName("mekanism.common.recipe.ShapedMekanismRecipe");
			if (c.isInstance(recipe)) return new ShapedMekanismRecipeHandler(c);

			c = Class.forName("mekanism.common.recipe.ShapelessMekanismRecipe");
			if (c.isInstance(recipe)) return new ShapelessMekanismRecipeHandler(c);
			
		}
		catch(Exception ex) { 
		}
		
		
		return null;
	}
	
	
	public static void postInit()
	{
	}

	
	
	private static class ItemStackHelper 
	{

	    public static boolean areItemsEqualIgnoreDurability(@Nullable ItemStack stackA, @Nullable ItemStack stackB)
	    {
	        return stackA == stackB ? true : (stackA != null && stackB != null ? isItemEqualIgnoreDurability(stackA, stackB) : false);
	    }
	    
	    public static boolean isItemEqualIgnoreDurability(@Nullable ItemStack stackA, @Nullable ItemStack stackB)
	    {
	        return !stackA.isItemStackDamageable() ? stackA.isItemEqual(stackB) : stackB != null && stackA.getItem() == stackB.getItem();
	    }
	    
	}
	
}
