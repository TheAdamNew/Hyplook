package me.theadamnew.hyplook.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class PerspectiveTransformer implements IClassTransformer {

    private static final String ENTITY_RENDERER = "net.minecraft.client.renderer.EntityRenderer";
    private static final String ENTITY = "net/minecraft/entity/Entity";
    private static final String HOOKS = "me/theadamnew/hyplook/asm/hooks/CameraHooks";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null || !ENTITY_RENDERER.equals(transformedName)) {
            return basicClass;
        }
        ClassReader reader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        boolean changed = false;
        for (MethodNode method : classNode.methods) {
            if (!"orientCamera".equals(method.name) && !"func_78467_g".equals(method.name)) {
                continue;
            }
            for (AbstractInsnNode insn : method.instructions.toArray()) {
                if (insn.getOpcode() != Opcodes.GETFIELD) {
                    continue;
                }
                FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                String hook = rotationHook(fieldInsn.owner, fieldInsn.name);
                if (hook == null) {
                    continue;
                }
                method.instructions.insertBefore(insn,
                        new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS, hook, "(Lnet/minecraft/entity/Entity;)F", false));
                method.instructions.remove(insn);
                changed = true;
            }
        }

        if (changed) {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        return basicClass;
    }

    private static String rotationHook(String owner, String fieldName) {
        if (!ENTITY.equals(owner)) {
            return null;
        }
        if ("rotationYaw".equals(fieldName) || "field_70177_z".equals(fieldName)) {
            return "rotationYaw";
        }
        if ("prevRotationYaw".equals(fieldName) || "field_70126_B".equals(fieldName)) {
            return "prevRotationYaw";
        }
        if ("rotationPitch".equals(fieldName) || "field_70125_A".equals(fieldName)) {
            return "rotationPitch";
        }
        if ("prevRotationPitch".equals(fieldName) || "field_70127_C".equals(fieldName)) {
            return "prevRotationPitch";
        }
        return null;
    }
}