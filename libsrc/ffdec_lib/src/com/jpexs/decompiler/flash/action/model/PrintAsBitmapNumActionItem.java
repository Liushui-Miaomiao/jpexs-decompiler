/*
 *  Copyright (C) 2010-2024 JPEXS, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.jpexs.decompiler.flash.action.model;

import com.jpexs.decompiler.flash.SourceGeneratorLocalData;
import com.jpexs.decompiler.flash.action.model.operations.AddActionItem;
import com.jpexs.decompiler.flash.action.parser.script.ActionSourceGenerator;
import com.jpexs.decompiler.flash.action.swf4.ActionGetURL2;
import com.jpexs.decompiler.flash.action.swf4.ActionPush;
import com.jpexs.decompiler.flash.ecma.Undefined;
import com.jpexs.decompiler.flash.helpers.GraphTextWriter;
import com.jpexs.decompiler.graph.CompilationException;
import com.jpexs.decompiler.graph.GraphSourceItem;
import com.jpexs.decompiler.graph.GraphTargetItem;
import com.jpexs.decompiler.graph.GraphTargetVisitorInterface;
import com.jpexs.decompiler.graph.SourceGenerator;
import com.jpexs.decompiler.graph.model.LocalData;
import java.util.List;
import java.util.Objects;

/**
 * Print as bitmap num.
 * @author JPEXS
 */
public class PrintAsBitmapNumActionItem extends ActionItem {

    private final GraphTargetItem num;

    private final GraphTargetItem boundingBox;

    @Override
    public void visit(GraphTargetVisitorInterface visitor) {
        visitor.visit(num);
        visitor.visit(boundingBox);
    }

    public PrintAsBitmapNumActionItem(GraphSourceItem instruction, GraphSourceItem lineStartIns, GraphTargetItem num, GraphTargetItem boundingBox) {
        super(instruction, lineStartIns, PRECEDENCE_PRIMARY);
        this.num = num;
        this.boundingBox = boundingBox;
    }

    @Override
    public GraphTextWriter appendTo(GraphTextWriter writer, LocalData localData) throws InterruptedException {
        writer.append("printAsBitmapNum");
        writer.spaceBeforeCallParenthesies(2);
        writer.append("(");
        num.toString(writer, localData);
        writer.append(",");
        boundingBox.toString(writer, localData);
        return writer.append(")");
    }

    @Override
    public List<GraphSourceItem> toSourceIgnoreReturnValue(SourceGeneratorLocalData localData, SourceGenerator generator) throws CompilationException {
        return toSource(localData, generator, false);
    }

    @Override
    public List<GraphSourceItem> toSource(SourceGeneratorLocalData localData, SourceGenerator generator) throws CompilationException {
        return toSource(localData, generator, true);
    }

    private List<GraphSourceItem> toSource(SourceGeneratorLocalData localData, SourceGenerator generator, boolean needsReturn) throws CompilationException {
        ActionSourceGenerator asGenerator = (ActionSourceGenerator) generator;
        String charset = asGenerator.getCharset();
        Object lev;
        if ((num instanceof DirectValueActionItem) && (((DirectValueActionItem) num).value instanceof Long)) {
            lev = asGenerator.pushConstTargetItem("_level" + ((DirectValueActionItem) num).value);
        } else {
            lev = new AddActionItem(getSrc(), getLineStartItem(), asGenerator.pushConstTargetItem("_level"), num, true);
        }
        return toSourceMerge(localData, generator, new AddActionItem(getSrc(), getLineStartItem(), asGenerator.pushConstTargetItem("printasbitmap:#"), boundingBox, true), lev, new ActionGetURL2(0, false, false, charset), needsReturn ? new ActionPush(new Object[]{Undefined.INSTANCE, Undefined.INSTANCE}, charset) : null);
    }

    @Override
    public boolean hasReturnValue() {
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.num);
        hash = 97 * hash + Objects.hashCode(this.boundingBox);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PrintAsBitmapNumActionItem other = (PrintAsBitmapNumActionItem) obj;
        if (!Objects.equals(this.num, other.num)) {
            return false;
        }
        if (!Objects.equals(this.boundingBox, other.boundingBox)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean valueEquals(GraphTargetItem obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PrintAsBitmapNumActionItem other = (PrintAsBitmapNumActionItem) obj;
        if (!GraphTargetItem.objectsValueEquals(this.num, other.num)) {
            return false;
        }
        if (!GraphTargetItem.objectsValueEquals(this.boundingBox, other.boundingBox)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasSideEffect() {
        return true;
    }
}
