package me.mateusz.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

fun createItem(name: String, material : Material, number : Int) : ItemStack {
    val item : ItemStack = ItemStack(material, number)
    val meta : ItemMeta = item.itemMeta as ItemMeta
    meta.setDisplayName(name)
    item.itemMeta = meta
    return item
}

fun createItem(name: String, material : Material, number : Int, lore : List<String>) : ItemStack {
    val item : ItemStack = ItemStack(material, number)
    val meta : ItemMeta = item.itemMeta as ItemMeta
    meta.setDisplayName(name)
    meta.lore = lore
    item.itemMeta = meta
    return item
}

fun createItem(name: String, material : Material, number : Int, lore : List<String>, enchant : Enchantment, enchantLvL : Int, hideEnchants : Boolean) : ItemStack {
    val item : ItemStack = ItemStack(material, number)
    val meta : ItemMeta = item.itemMeta as ItemMeta
    meta.setDisplayName(name)
    meta.lore = lore
    meta.addEnchant(enchant, enchantLvL, true)
    if(hideEnchants) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    item.itemMeta = meta
    return item
}

fun createItem(name: String, material : Material, number : Int, enchant : Enchantment, enchantLvL : Int, hideEnchants : Boolean) : ItemStack {
    val item : ItemStack = ItemStack(material, number)
    val meta : ItemMeta = item.itemMeta as ItemMeta
    meta.setDisplayName(name)
    meta.addEnchant(enchant, enchantLvL, true)
    if(hideEnchants) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    item.itemMeta = meta
    return item
}