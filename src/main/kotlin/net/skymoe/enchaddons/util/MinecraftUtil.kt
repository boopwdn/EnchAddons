package net.skymoe.enchaddons.util

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.scoreboard.ScoreObjective
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.BlockPos
import net.minecraft.util.IChatComponent
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Vec3i
import net.skymoe.enchaddons.feature.config.Color
import net.skymoe.enchaddons.getLogger
import net.skymoe.enchaddons.util.math.*

private val logger = getLogger("Minecraft Util")

val MC: Minecraft by lazy { Minecraft.getMinecraft() }

var tickCounter = 0L

var partialTicks = 0.0

var mousePosition = Vec2D(0.0, 0.0)

var guiScale = 1.0

inline val Vec3i.asVec3I get() = Vec3I(x, y, z)

inline val Vec3I.asBlockPos get() = BlockPos(x, y, z)

inline val Entity.lastPos get() = Vec3D(prevPosX, prevPosY, prevPosZ)

inline val Entity.currPos get() = Vec3D(posX, posY, posZ)

inline val Entity.renderPos get() = lerp(lastPos, currPos, partialTicks)

val REGEX_STYLE = Regex("\\u00a7.")

inline val String.removeStyle get() = REGEX_STYLE.replace(this, "")

inline val String.trimStyle get() = removeStyle.trim()

inline val String.keepASCII get() = filter { it.code in 32..126 }

inline val Block.item: Item get() = Item.getItemFromBlock(this)

inline fun mcRenderScope(
    mode: Int,
    vertexFormat: VertexFormat,
    function: WorldRenderer.() -> Unit,
) {
    val tessellator = Tessellator.getInstance()
    val worldRenderer = tessellator.worldRenderer
    worldRenderer.begin(mode, vertexFormat)
    try {
        function(worldRenderer)
    } finally {
        tessellator.draw()
    }
}

fun WorldRenderer.pos(vec: Vec3D): WorldRenderer = pos(vec.x, vec.y, vec.z)

fun WorldRenderer.tex(vec: Vec2D): WorldRenderer = tex(vec.x, vec.y)

fun WorldRenderer.color(color: Color): WorldRenderer = color(color.r.float, color.g.float, color.b.float, color.a.float)

fun printChat(message: IChatComponent) {
    logger.info("[PRINT CHAT] $message")
    MC.theWorld?.let {
        MC.ingameGUI.chatGUI.printChatMessage(message)
    }
}

fun printChat(message: String = "") {
    printChat(message.asComponent())
}

fun printChat(throwable: Throwable) = printChat(throwable.stackTraceMessage)

class CustomSound(
    private val argSoundLocation: ResourceLocation,
    private val argVolume: Double = 1.0,
    private val argPitch: Double = 1.0,
    private val argCanRepeat: Boolean = false,
    private val argRepeatDelay: Int = 0,
    private val argXPos: Double = 0.0,
    private val argYPos: Double = 0.0,
    private val argZPos: Double = 0.0,
    private val argAttenuationType: ISound.AttenuationType = ISound.AttenuationType.NONE,
) : ISound {
    override fun getSoundLocation() = argSoundLocation

    override fun canRepeat() = argCanRepeat

    override fun getRepeatDelay() = argRepeatDelay

    override fun getVolume() = argVolume.toFloat()

    override fun getPitch() = argPitch.toFloat()

    override fun getXPosF() = argXPos.toFloat()

    override fun getYPosF() = argYPos.toFloat()

    override fun getZPosF() = argZPos.toFloat()

    override fun getAttenuationType() = argAttenuationType
}

fun updateWorldRender() {
    MC.renderGlobal.loadRenderers()
}

fun updateWorldRenderArea(area: Area3I) {
    MC.renderGlobal.markBlockRangeForRenderUpdate(
        area.first.x,
        area.first.y,
        area.first.z,
        area.second.x - 1,
        area.second.y - 1,
        area.second.z - 1,
    )
}

fun updateWorldRenderBlock(pos: Vec3I) {
    MC.renderGlobal.markBlockRangeForRenderUpdate(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z)
}

data class SideBar(
    val title: String,
    val list: List<Entry>,
) {
    data class Entry(
        val name: String,
        val value: Int,
    )
}

inline val sideBar: SideBar?
    get() {
        MC.theWorld ?: return null
        val scoreboard = MC.theWorld.scoreboard
        var objective: ScoreObjective? = null
        val team = scoreboard.getPlayersTeam(MC.thePlayer.name)
        if (team !== null) {
            val color = team.chatFormat.colorIndex
            if (color >= 0) {
                objective = scoreboard.getObjectiveInDisplaySlot(3 + color)
            }
        }
        objective = objective ?: scoreboard.getObjectiveInDisplaySlot(1) ?: return null
        return SideBar(
            objective.displayName,
            scoreboard
                .getSortedScores(objective)
                .reversed()
                .filter { s -> s.playerName !== null && !s.playerName.startsWith("#") }
                .take(15)
                .map {
                    SideBar.Entry(
                        ScorePlayerTeam.formatPlayerName(
                            scoreboard.getPlayersTeam(it.playerName),
                            it.playerName,
                        ),
                        it.scorePoints,
                    )
                },
        )
    }
