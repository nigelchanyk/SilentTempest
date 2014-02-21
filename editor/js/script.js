String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

// Class Core
Core = function(canvas) {
    this.canvas = canvas;
    this.subscribers = {
        onBeforeCellsChanged: Array(),
        onCellChanged: Array(),
        onLayerChanged: Array(),
        onLayerCountChanged: Array(),
        onLayerGroupChanged: Array(),
        onLayerRemoved: Array(),
        onLevelReset: Array(),
        onPaletteAreaChanged: Array()
    };
    this.setLevel(new Level(1, 1, 15, 20));
    this.setPaletteArea(new Area(0, 0, 1, 1));
};

Core.LayerGroup = {};
Core.LayerGroup.GROUND = 0;
Core.LayerGroup.OBJECT = 1;
Core.LayerGroup.EVENT = 2;

Core.prototype.bind = function(evts, callback) {
    var evtTokens = evts.split(' ');
    for (var i = 0; i < evtTokens.length; ++i)
        this.subscribers[evtTokens[i]].push(callback);
};

Core.prototype.getLevel = function(level) {
    return this.level;
};

Core.prototype.notify = function(evt) {
    var args = Array.prototype.slice.call(arguments, 1);
    var subscribers = this.subscribers[evt];
    for (var i = 0; i < subscribers.length; ++i)
        subscribers[i].apply(this, args);
};

Core.prototype.setLevel = function(level) {
    this.level = level;
    this.setLayerGroup(Core.LayerGroup.GROUND);
    this.notify('onLevelReset', level);
};

Core.prototype.getPaletteArea = function() {
    return this.paletteArea;
};

Core.prototype.setPaletteArea = function(area) {
    this.paletteArea = area;
    this.notify('onPaletteAreaChanged', area);
};

Core.prototype.getPaletteWidth = function() {
    return this.paletteWidth;
};

Core.prototype.setPaletteWidth = function(width) {
    if (width % 1 !== 0)
        throw 'Palette width is not % 32';
    this.paletteWidth = width;
};

Core.prototype.getPaletteHeight = function() {
    return this.paletteHeight;
};

Core.prototype.setPaletteHeight = function(height) {
    if (height % 1 !== 0)
        throw 'Palette height is not % 32';
    this.paletteHeight = height;
};

Core.prototype.getLayer = function() {
    return this.layer;
};

Core.prototype.setLayer = function(layer) {
    this.layer = layer;
    this.notify('onLayerChanged', layer);
};

Core.prototype.getLayerGroup = function(layerGroup) {
    return this.layerGroup;
};

Core.prototype.setLayerGroup = function(layerGroup) {
    this.setLayer(0);
    this.layerGroup = layerGroup;
    this.notify('onLayerGroupChanged', layerGroup);
};

Core.prototype.paint = function(changes) {
    this.notify('onBeforeCellsChanged', changes);
    for (var i = 0; i < changes.length; ++i) {
        if (this.layerGroup === Core.LayerGroup.GROUND)
            this.level.setGroundGrid(this.layer, changes[i]['row'], changes[i]['column'], changes[i]['index']);
        else
            this.level.setTopGrid(this.layer, changes[i]['row'], changes[i]['column'], changes[i]['index']);
        this.notify('onCellChanged', changes[i]['row'], changes[i]['column']);
    }
};

Core.prototype.addLayer = function() {
    var count = 0;
    if (this.layerGroup === Core.LayerGroup.GROUND) {
        this.level.addGroundLayer();
        count = this.level.getGroundDepthCount();
    }
    else if (this.layerGroup === Core.LayerGroup.OBJECT) {
        this.level.addTopLayer();
        count = this.level.getTopDepthCount();
    }
    this.notify('onLayerCountChanged', count);
    this.setLayer(count - 1);
};

Core.prototype.removeLayer = function() {
    var count = 0;
    if (this.layerGroup === Core.LayerGroup.GROUND) {
        if (this.level.getGroundDepthCount() === 1)
            return;
        this.level.removeGroundLayer(this.layer);
        count = this.level.getGroundDepthCount();
    }
    else if (this.layerGroup === Core.LayerGroup.OBJECT) {
        if (this.level.getTopDepthCount() === 1)
            return;
        this.level.removeTopLayer(this.layer);
        count = this.level.getTopDepthCount();
    }
    this.notify('onLayerCountChanged', count);
    this.notify('onLayerRemoved');
    this.setLayer(this.layer);
};

// Class Area {
Area = function(row, column, width, height) {
    this.row = row;
    this.column = column;
    this.width = width;
    this.height = height;
};

Area.prototype.getRow = function() {
    return this.row;
};

Area.prototype.getColumn = function() {
    return this.column;
};

Area.prototype.getWidth = function() {
    return this.width;
};

Area.prototype.getHeight = function() {
    return this.height;
};

Area.prototype.distinct = function(other) {
    var coordinates = {};
    Area._setMinus(this, other, coordinates);
    Area._setMinus(other, this, coordinates);
    var result = Array();
    for (var r in coordinates) {
        for (var c in coordinates[r]) {
            result.push({
                row: r,
                column: c
            });
        }
    }
    return result;
};

Area.prototype.encloses = function(row, column) {
    return row >= this.row && row < this.row + this.height && column >= this.column && column < this.column + this.width;
};

Area.fromDisplayCoordinate = function(startX, startY, endX, endY, width, height) {
    var maxX = width - 1;
    var maxY = height - 1;
    startX = ~~(Math.min(startX, maxX) / 32);
    startY = ~~(Math.min(startY, maxY) / 32);
    endX = ~~(Math.min(endX, maxX) / 32);
    endY = ~~(Math.min(endY, maxY) / 32);
    return new Area(
        Math.min(startY, endY),
        Math.min(startX, endX),
        Math.abs(endX - startX) + 1,
        Math.abs(endY - startY) + 1
    );
};

Area._setMinus = function(a, b, coordinates) {
    for (var r = 0; r < a.height; ++r) {
        for (var c = 0; c < a.width; ++c) {
            var row = r + a.row;
            var col = c + a.column;
            if (row < b.row || row >= b.row + b.height || col < b.column || col >= b.column + b.width) {
                if (coordinates[row] === undefined)
                    coordinates[row] = {};
                coordinates[row][col] = true;
            }
        }
    }
};

// Class HistoryManager
HistoryManager = function(core, btnGroup) {
    this.core = core;
    this.btnGroup = btnGroup;
    this.ignoreSignal = false;
    this.undoStack = new Array();
    this.redoStack = new Array();
    var _this = this;
    $('.btn-undo', btnGroup).bind('click', function() {
        _this.onUndo();
    });
    $('.btn-redo', btnGroup).bind('click', function() {
        _this.onRedo();
    });
    core.bind('onBeforeCellsChanged', function(changes) {
        _this.onBeforeCellsChanged(changes);
    });
    core.bind('onLayerRemoved onLevelReset', function() {
        _this.reset();
    });
};

HistoryManager.prototype.onBeforeCellsChanged = function(changes) {
    if (this.ignoreSignal)
        return;
    this.redoStack.length = 0;
    if (this.undoStack.length === 100)
        this.undoStack.shift();
    this.undoStack.push(this.getSnapshot({
        'layer': this.core.getLayer(),
        'layerGroup': this.core.getLayerGroup(),
        'changes': changes
    }));
    this.changeUIState();
};

HistoryManager.prototype.changeUIState = function() {
    if (this.undoStack.length === 0)
        $('.btn-undo', this.btnGroup).addClass('disabled');
    else
        $('.btn-undo', this.btnGroup).removeClass('disabled');
    if (this.redoStack.length === 0)
        $('.btn-redo', this.btnGroup).addClass('disabled');
    else
        $('.btn-redo', this.btnGroup).removeClass('disabled');
};

HistoryManager.prototype.onUndo = function() {
    if (this.undoStack.length === 0)
        return;
    var state = this.undoStack.pop();
    this.redoStack.push(this.getSnapshot(state));
    this.ignoreSignal = true;
    this.core.setLayerGroup(state['layerGroup']);
    this.core.setLayer(state['layer']);
    this.core.paint(state['changes']);
    this.ignoreSignal = false;
    this.changeUIState();
};

HistoryManager.prototype.onRedo = function() {
    if (this.redoStack.length === 0)
        return;
    var state = this.redoStack.pop();
    this.undoStack.push(this.getSnapshot(state));
    this.ignoreSignal = true;
    this.core.setLayerGroup(state['layerGroup']);
    this.core.setLayer(state['layer']);
    this.core.paint(state['changes']);
    this.ignoreSignal = false;
    this.changeUIState();
};

HistoryManager.prototype.getSnapshot = function(state) {
    var reverseChanges = new Array();
    var layer = state['layer'];
    var layerGroup = state['layerGroup'];
    var changes = state['changes'];
    for (var i = 0; i < changes.length; ++i) {
        var change = changes[i];
        reverseChanges.push({
            row: change['row'],
            column: change['column'],
            index: this.core.getLevel().getGrid(layerGroup, layer, change['row'], change['column'])
        });
    }
    return {
        'layer': layer,
        'layerGroup': layerGroup,
        'changes': reverseChanges
    };
};

HistoryManager.prototype.reset = function() {
    this.undoStack.length = 0;
    this.redoStack.length = 0;
};

// Class Palette
Palette = function(core, palette, fieldSprite) {
    this.core = core;
    this.palette = palette;
    this.fieldSprite = $(fieldSprite)[0];
    this.startX = 0;
    this.startY = 0;
    this.endX = 0;
    this.endY = 0;
    this.dragging = false;
    this.core.setPaletteWidth(this.fieldSprite.width / 32);
    this.core.setPaletteHeight(this.fieldSprite.height / 32);
    $(palette).css({
        background: 'url(' + this.fieldSprite.src + ') 0 0',
        width: this.fieldSprite.width,
        height: this.fieldSprite.height
    });
    var _this = this;
    $(palette).bind('mousedown', function(evt) {
        _this.onMouseDown(evt);
    });
    $(palette).bind('mousemove', function(evt) {
        _this.onMouseMove(evt);
    });
    $(palette).bind('mouseup mouseleave', function(evt) {
        _this.onMouseUp(evt);
    });
    core.bind('onPaletteAreaChanged', function(area) {
        $('.selector', _this.palette).css({
            'margin-left': area.getColumn() * 32,
            'margin-top': area.getRow() * 32,
            width: area.getWidth() * 32,
            height: area.getHeight() * 32
        });
    });
};

Palette.prototype.onMouseDown = function(evt) {
    var viewportOffset = $(palette)[0].getBoundingClientRect();
    this.startX = this.endX = evt.clientX - viewportOffset.left;
    this.startY = this.endY = evt.clientY - viewportOffset.top;
    this.dragging = true;
    this.changeArea();
};

Palette.prototype.onMouseMove = function(evt) {
    if (!this.dragging)
        return;
    var viewportOffset = $(palette)[0].getBoundingClientRect();
    this.endX = evt.clientX - viewportOffset.left;
    this.endY = evt.clientY - viewportOffset.top;
    this.changeArea();
};

Palette.prototype.onMouseUp = function(evt) {
    if (!this.dragging)
        return;
    this.dragging = false;
    var viewportOffset = $(palette)[0].getBoundingClientRect();
    this.endX = evt.clientX - viewportOffset.left;
    this.endY = evt.clientY - viewportOffset.top;
    this.changeArea();
};

Palette.prototype.changeArea = function() {
    this.core.setPaletteArea(Area.fromDisplayCoordinate(this.startX, this.startY, this.endX, this.endY, $(this.palette).width(), $(this.palette).height()));
};

// Class LayerManager
LayerManager = function(core, layerManager) {
    this.core = core;
    this.layerManager = layerManager;
    var _this = this;
    $('.btn-ground', layerManager).bind('click', function() {
        _this.core.setLayerGroup(Core.LayerGroup.GROUND);
    });
    $('.btn-object', layerManager).bind('click', function() {
        _this.core.setLayerGroup(Core.LayerGroup.OBJECT);
    });
    $('.btn-event', layerManager).bind('click', function() {
        _this.core.setLayerGroup(Core.LayerGroup.EVENT);
    });
    $('.btn-add-layer', layerManager).bind('click', function() {
        _this.core.addLayer();
    });
    $('.btn-remove-layer', layerManager).bind('click', function() {
        window.showConfirmDialog('Are you sure you want to remove this layer?', 'Remove Layer', function() {
            _this.core.removeLayer();
        });
    });
    core.bind('onLayerChanged', function(layer) {
        $('.selected-layer', _this.layerManager).text(layer);
    });
    core.bind('onLayerGroupChanged', function(layerGroup) {
        _this.onLayerGroupChanged(layerGroup);
    });
    core.bind('onLayerCountChanged', function(count) {
        _this.onLayerCountChanged(count);
    });
};

LayerManager.prototype.onLayerGroupChanged = function(layerGroup) {
    $('.dropdown', this.layerManager).removeClass('open');
    $('.dropdown-item-layer', this.layerManager).remove();
    $('.group-control button', this.layerManager).removeClass('disabled');
    $('.layer-control', this.layerManager).css('visibility', layerGroup === Core.LayerGroup.EVENT ? 'hidden' : 'visible');
    switch (layerGroup) {
        case Core.LayerGroup.GROUND:
            this.createDropdownLayer(this.core.getLevel().getGroundDepthCount());
            $('.btn-ground', this.layerManager).addClass('disabled');
            return;
        case Core.LayerGroup.OBJECT:
            this.createDropdownLayer(this.core.getLevel().getTopDepthCount());
            $('.btn-object', this.layerManager).addClass('disabled');
            return;
        case Core.LayerGroup.EVENT:
            $('.btn-event', this.layerManager).addClass('disabled');
            return;
    }
};

LayerManager.prototype.createDropdownLayer = function(count) {
    var dropdown = $('.layer-control .dropdown-menu', this.groupControl)[0];
    for (var i = count - 1; i >= 0; i--) {
        var a = document.createElement('a');
        a.setAttribute('href' , '#');
        a.innerHTML = i + '';
        var li = document.createElement('li');
        li.setAttribute('class', 'dropdown-item-layer');
        li.setAttribute('data-layer', i + '');
        li.appendChild(a);
        dropdown.appendChild(li);
    }
    var _this = this;
    $('.dropdown-item-layer', this.layerManager).bind('click', function() {
        _this.core.setLayer($(this).data('layer'));
    });
};

LayerManager.prototype.onLayerCountChanged = function(count) {
    $('.dropdown-item-layer', this.layerManager).remove();
    this.createDropdownLayer(count);
    if (count > 1)
        $('.btn-remove-layer', this.layerManager).removeClass('disabled');
    else
        $('.btn-remove-layer', this.layerManager).addClass('disabled');
};

// Class Canvas
Canvas = function(core, canvas, btnGroup, fieldSprite) {
    this.core = core;
    this.canvas = canvas;
    this.btnGroup = btnGroup;
    this.fieldSprite = $(fieldSprite)[0];
    this.ctx = $(canvas)[0].getContext('2d');
    this.startX = 0;
    this.startY = 0;
    this.endX = 0;
    this.endY = 0;
    this.dragging = false;
    this.previewArea = null;
    this.mode = Canvas.Mode.DRAW;
    var _this = this;
    $('.btn-draw', btnGroup).bind('click', function() {
        _this.onChangeMode(Canvas.Mode.DRAW);
    });
    $('.btn-erase', btnGroup).bind('click', function() {
        _this.onChangeMode(Canvas.Mode.ERASE);
    });
    $(canvas).bind('mousedown', function(evt) {
        _this.onMouseDown(evt);
    });
    $(canvas).bind('mousemove', function(evt) {
        _this.onMouseMove(evt);
    });
    $(canvas).bind('mouseup', function(evt) {
        _this.onMouseUp(evt);
    });
    $(canvas).bind('mouseleave', function() {
        _this.onBlur();
    });
    $(window).bind('blur', function() {
        _this.onBlur();
    });
    core.bind('onLevelReset onLayerChanged onLayerGroupChanged', function() {
        _this.repaint();
    });
    core.bind('onCellChanged', function(row, column) {
        _this.paintCell(row, column);
    });
    this.repaint(core.getLevel());
};

Canvas.Mode = {};
Canvas.Mode.DRAW = 0;
Canvas.Mode.ERASE = 1;

Canvas.prototype.onChangeMode = function(mode) {
    this.mode = mode;
    if (mode === Canvas.Mode.DRAW) {
        $('.btn-draw', this.btnGroup).addClass('disabled');
        $('.btn-erase', this.btnGroup).removeClass('disabled');
        $(this.canvas).css('cursor', 'default');
    }
    else if (mode === Canvas.Mode.ERASE) {
        $('.btn-draw', this.btnGroup).removeClass('disabled');
        $('.btn-erase', this.btnGroup).addClass('disabled');
        $(this.canvas).css('cursor', 'crosshair');
    }
};

Canvas.prototype.onMouseDown = function(evt) {
    this.startX = this.endX = evt.offsetX;
    this.startY = this.endY = evt.offsetY;
    this.dragging = true;
    this.previewArea = null;
    this.paintPreview();
};

Canvas.prototype.onMouseMove = function(evt) {
    if (!this.dragging)
        return;
    this.endX = evt.offsetX;
    this.endY = evt.offsetY;
    this.paintPreview();
};

Canvas.prototype.onMouseUp = function(evt) {
    if (!this.dragging)
        return;
    this.endX = evt.offsetX;
    this.endY = evt.offsetY;
    this.dragging = false;
    var newArea = Area.fromDisplayCoordinate(this.startX, this.startY, this.endX, this.endY, this.width, this.height);
    var changes = Array();
    for (var r = newArea.getRow(); r < newArea.getRow() + newArea.getHeight(); ++r) {
        for (var c = newArea.getColumn(); c < newArea.getColumn() + newArea.getWidth(); ++c) {
            changes.push({
                row: r,
                column: c,
                index: this.mode === Canvas.Mode.DRAW ? this.getPaintIndex(r, c) : -1
            });
        }
    }
    this.core.paint(changes);
};

Canvas.prototype.onBlur = function() {
    if (!this.dragging)
        return;
    this.dragging = false;
    this.repaint();
};

Canvas.prototype.paintPreview = function() {
    if (this.previewArea === null) {
        var a = this.previewArea = Area.fromDisplayCoordinate(this.startX, this.startY, this.endX, this.endY, this.width, this.height);
        this.startRow = this.previewArea.getRow();
        this.startColumn = this.previewArea.getColumn();
        for (var r = a.getRow(); r < a.getRow() + a.getHeight(); ++r) {
            for (var c = a.getColumn(); c < a.getColumn() + a.getWidth(); ++c) {
                this.paintCell(r, c);
            }
        }
    } else {
        var newArea = Area.fromDisplayCoordinate(this.startX, this.startY, this.endX, this.endY, this.width, this.height);
        var distinct = newArea.distinct(this.previewArea);
        this.previewArea = newArea;
        for (var i = 0; i < distinct.length; ++i)
            this.paintCell(distinct[i]['row'], distinct[i]['column']);
    }
};

Canvas.prototype.repaint = function() {
    this.width = this.core.getLevel().getColumnCount() * 32;
    this.height = this.core.getLevel().getRowCount() * 32;
    $(this.canvas).attr({
        width: this.width,
        height: this.height
    });
    var rows = this.core.getLevel().getRowCount();
    var cols = this.core.getLevel().getColumnCount();
    for (var r = 0; r < rows; ++r) {
        for (var c = 0; c < cols; ++c)
            this.paintCell(r, c);
    }
};

Canvas.prototype.paintCell = function(row, column) {
    this.ctx.fillStyle = 'rgb(255,255,255)';
    this.ctx.fillRect(column * 32, row * 32, 32, 32);
    var groundDepth = this.core.getLevel().getGroundDepthCount();
    for (var i = 0; i < groundDepth; ++i) {
        this.ctx.globalAlpha = this.core.getLayerGroup() === Core.LayerGroup.GROUND && this.core.getLayer() === i ? 1 : 0.3;
        var index = this.isPreviewCellLayer(row, column, i, Core.LayerGroup.GROUND)
            ? this.getPaintIndex(row, column)
            : this.core.getLevel().getGroundGrid(i, row, column);
        this.paintCellLayer(row, column, index);
    }
    var topDepth = this.core.getLevel().getTopDepthCount();
    for (var i = 0; i < topDepth; ++i) {
        this.ctx.globalAlpha = this.core.getLayerGroup() === Core.LayerGroup.OBJECT && this.core.getLayer() === i ? 1 : 0.3;
        var index = this.isPreviewCellLayer(row, column, i, Core.LayerGroup.OBJECT)
            ? this.getPaintIndex(row, column)
            : this.core.getLevel().getTopGrid(i, row, column);
        this.paintCellLayer(row, column, index);
    }
    this.ctx.globalAlpha = 1;
};

Canvas.prototype.paintCellLayer = function(row, column, index) {
    if (index < 0)
        return;
    this.ctx.drawImage(this.fieldSprite, (index % 5) * 32, (~~(index / 5)) * 32, 32, 32, column * 32, row * 32, 32, 32);
};

Canvas.prototype.isPreviewCellLayer = function(row, column, layer, layerGroup) {
    return this.dragging &&
        this.previewArea !== null &&
        this.previewArea.encloses(row, column) &&
        this.core.getLayerGroup() === layerGroup &&
        this.core.getLayer() === layer;
};

Canvas.prototype.getPaintIndex = function(row, column) {
    if (this.mode === Canvas.Mode.ERASE)
        return -1;
    var palette = this.core.getPaletteArea();
    var offsetRow = ((row - this.startRow) % palette.getHeight() + palette.getHeight()) % palette.getHeight() + palette.getRow();
    var offsetCol = ((column - this.startColumn) % palette.getWidth() + palette.getWidth()) % palette.getWidth() + palette.getColumn();
    return offsetRow * this.core.getPaletteWidth() + offsetCol;
};

// Class Level
Level = function(groundDepth, topDepth, row, column) {
    this.row = row;
    this.column = column;
    this.groundDepth = groundDepth;
    this.topDepth = topDepth;

    this.groundLayers = this.createArray(row, column, groundDepth);
    this.topLayers = this.createArray(row, column, topDepth);
};

Level.prototype.createArray = function(row, column, depth) {
    var layers = new Array(depth);
    for (var i = 0; i < depth; ++i)
        layers[i] = this.createLayer(row, column);
    return layers;
}

Level.prototype.createLayer = function(row, column) {
    layer = new Array(row);
    for (var j = 0; j < row; ++j) {
        layer[j] = new Array(column);
        for (var k = 0; k < column; ++k) {
            layer[j][k] = -1;
        }
    }
    return layer;
};

Level.prototype.getRowCount = function() {
    return this.row;
};

Level.prototype.getColumnCount = function() {
    return this.column;
};

Level.prototype.getGroundDepthCount = function() {
    return this.groundDepth;
};

Level.prototype.getTopDepthCount = function() {
    return this.topDepth;
};

Level.prototype.getGroundGrid = function(depth, row, column) {
    return this.groundLayers[depth][row][column];
};

Level.prototype.setGroundGrid = function(depth, row, column, index) {
    this.groundLayers[depth][row][column] = index;
};

Level.prototype.getGrid = function(layerGroup, depth, row, column) {
    return (layerGroup === Core.LayerGroup.GROUND ? this.groundLayers : this.topLayers)[depth][row][column];
};

Level.prototype.setGrid = function(layerGroup, depth, row, column, index) {
    (layerGroup === Core.LayerGroup.GROUND ? this.groundLayers : this.topLayers)[depth][row][column] = index;
};

Level.prototype.getTopGrid = function(depth, row, column) {
    return this.topLayers[depth][row][column];
};

Level.prototype.setTopGrid = function(depth, row, column, index) {
    this.topLayers[depth][row][column] = index;
};

Level.prototype.addGroundLayer = function() {
    this.groundLayers.push(this.createLayer(this.row, this.column));
    this.groundDepth++;
};

Level.prototype.addTopLayer = function() {
    this.topLayers.push(this.createLayer(this.row, this.column));
    this.topDepth++;
};

Level.prototype.removeGroundLayer = function(index) {
    if (this.groundDepth === 1)
        return;
    this.groundLayers.splice(index, 1);
    this.groundDepth--;
};

Level.prototype.removeTopLayer = function(index) {
    if (this.topDepth === 1)
        return;
    this.topLayers.splice(index, 1);
    this.topDepth--;
};

// Class LevelImporter
LevelImporter = function(core) {
    this.core = core;
};

LevelImporter.Errors = {};
LevelImporter.Errors.FileNotFound = 'The file cannot be found.';
LevelImporter.Errors.FileNotReadable = 'Unable to read the file.';
LevelImporter.Errors.FileLocked = 'Read permission denied. The file is locked.';
LevelImporter.Errors.GenericError = 'An error occurred while reading the file.';
LevelImporter.Errors.SyntaxError = 'The file contains malformed JSON.';
LevelImporter.REGEX = /^(?:(?:([a-z0-9]+)>)|())([a-z0-9]*)(?:(?:\:([a-z0-9]+))|())$/;

LevelImporter.prototype.load = function(file, errorCallback) {
    var reader = new window.FileReader();
    var _this = this;
    reader.onload = function(evt) {
        //try {
            var level = _this.parseLevel($.parseJSON(evt.target.result));
            _this.core.setLevel(level);
        /*} catch (e) {
            switch (e.name) {
                case 'SyntaxError':
                    errorCallback(LevelImporter.Errors.SyntaxError);
                    break;
                default:
                    errorCallback(LevelImporter.Errors.GenericError);
            }
        }*/
    };
    reader.onerror = function(evt) {
        switch (evt.target.error) {
            case e.target.error.NOT_FOUND_ERR:
                errorCallback(LevelImporter.Errors.FileNotFound);
                break;
            case e.target.error.NOT_READABLE_ERR:
                errorCallback(LevelImporter.Errors.FileNotReadable);
                break;
            case e.target.error.SECURITY_ERR:
                errorCallback(LevelImporter.Errors.FileLocked);
                break;
            default:
                errorCallback(LevelImporter.Errors.GenericError);
        }
    };
    reader.readAsText(file);
};

LevelImporter.prototype.getExtension = function() {
    return '.stl';
};

LevelImporter.prototype.parseLevel = function(json) {
    var rows = json['rows'];
    var columns = json['columns'];
    var groundLevels = json['ground'];
    var topLevels = json['top'];
    var level = new Level(groundLevels.length, topLevels.length, rows, columns);
    for (var i = 0; i < level.getGroundDepthCount(); ++i)
        this.parseLayer(level, Core.LayerGroup.GROUND, i, json['ground'][i]);
    for (var i = 0; i < level.getTopDepthCount(); ++i)
        this.parseLayer(level, Core.LayerGroup.OBJECT, i, json['top'][i]);

    return level;
};

LevelImporter.prototype.parseLayer = function(level, layerGroup, layer, data) {
    var rows = level.getRowCount();
    var columns = level.getColumnCount();
    var total = rows * columns;
    var tuples = data.split(';');
    for (var i = 0, j = 0; i < tuples.length && j < total; ++i) {
        var matches = tuples[i].match(LevelImporter.REGEX);
        var skip = parseInt(matches[1] || matches[2] || '0', 36);
        var index = parseInt(matches[3] || '-1', 36);
        var repeat = parseInt(matches[4] || matches[5] || '0', 36);
        j += skip;
        for (var r = 0; r <= repeat && j < total; ++r, ++j)
            level.setGrid(layerGroup, layer, ~~(j / columns), j % columns, index);
    }
};

LevelExporter = function(core) {
    this.core = core;
};

LevelExporter.prototype.transformLevel = function(level) {
    var json = {};
    json['rows'] = level.getRowCount();
    json['columns'] = level.getColumnCount();
    json['ground'] = [];
    for (var i = 0; i < level.getGroundDepthCount(); ++i)
        json['ground'].push(this.transformLayer(level, Core.LayerGroup.GROUND, i));
    json['top'] = [];
    for (var i = 0; i < level.getTopDepthCount(); ++i)
        json['top'].push(this.transformLayer(level, Core.LayerGroup.OBJECT, i));
    return json;
};

LevelExporter.prototype.transformLayer = function(level, layerGroup, layer) {
    var rows = level.getRowCount();
    var columns = level.getColumnCount();
    var result = [];
    var last = 0;
    var repeat = 0;
    var repeatedIndex = -1;
    for (var r = 0; r < rows; ++r) {
        for (var c = 0; c < columns; ++c) {
            var index = level.getGrid(layerGroup, layer, r, c);
            var current = r * columns + c;
            if (index < 0) {
                repeatedIndex = -1;
                repeat = 0;
                continue;
            }
            if (repeatedIndex === index) {
                repeat++;
                if (repeat === 1) {
                    result.push(':');
                    result.push(repeat.toString(36));
                } else {
                    result[result.length - 1] = repeat.toString(36);
                }
                last = current;
                continue;
            }
            if (current - last <= 3) {
                while (last < current) {
                    result.push(';');
                    last++;
                }
            } else {
                result.push(';');
                result.push((current - last - 1).toString(36));
                result.push('>');
            }
            result.push(index.toString(36));
            repeatedIndex = index;
            last = current;
            repeat = 0;
        }
    }
    return result.join('');
};

LevelExporter.prototype.toJSON = function() {
    return JSON.stringify(this.transformLevel(this.core.getLevel()));
};

LevelExporter.prototype.save = function(filename) {
    var blob = new Blob([this.toJSON()], {type: "text/plain;charset=utf-8"});
    saveAs(blob, filename);
};

// Class FileSelector
FileSelector = function(loader, btn, dialog) {
    this.loader = loader;
    this.dialog = dialog;
    this.file = null;
    var _this = this;
    $(btn).bind('click', function() {
        _this.onClick();
    });
    var holder = $('.drop-zone', dialog).get(0);
    holder.ondragover = function() {
        $('.drop-zone', dialog).addClass('hover');
        return false;
    }
    holder.ondragend = function() {
        $('.drop-zone', dialog).removeClass('hover');
        return false;
    }
    holder.ondrop = function(e) {
        return _this.onDrop(e);
    }
    $('.btn-confirm', this.dialog).bind('click', function() {
        return _this.onConfirm();
    });
};

FileSelector.prototype.onClick = function() {
    $(this.dialog).modal('show');
};

FileSelector.prototype.onDrop = function(e) {
    $('.drop-zone', this.dialog).removeClass('hover');
    e.preventDefault();
    if (!e.dataTransfer.files[0].name.endsWith('.stl')) {
        window.showMessage('Expecting Silent Tempest Level (*.stl) file.', 'Error');
        return false;
    }
    this.file = e.dataTransfer.files[0];
    $('.btn-confirm', this.dialog).removeClass('disabled');
    $('.btn-confirm', this.dialog).text('Open ' + this.file.name.substring(0, 50));
    return false;
};

FileSelector.prototype.onConfirm = function() {
    if (!this.file) {
        return false;
    }
    $(this.dialog).modal('hide');
    var _this = this;
    this.loader.load(this.file, function(message) {
        window.showMessage(message, 'Error');
    });
};

FileSaver = function(exporter, btn, filenameInput) {
    this.exporter = exporter;
    this.filenameInput = filenameInput;
    var _this = this;
    $(btn).bind('click', function() {
        _this.onClick();
    });
};

FileSaver.prototype.onClick = function() {
    var filename = $(this.filenameInput).val();
    filename = filename.replace(' ', '_');
    if (filename === '')
        filename = 'output.stl';
    if (!filename.toLowerCase().endsWith('.stl'))
        filename = filename + '.stl';
    this.exporter.save(filename);
};

window.showMessage = function(message, title) {
    $('#message-modal .modal-title').text(title);
    $('#message-modal .modal-message').text(message);
    $('#message-modal').modal('show');
};

window.confirmDialog = {};
window.confirmDialog.acceptCallback = null;

window.showConfirmDialog = function(message, title, acceptCallback) {
    $('#confirm-modal .modal-title').text(title);
    $('#confirm-modal .modal-message').text(message);
    window.confirmDialog.acceptCallback = acceptCallback;
    $('#confirm-modal').modal('show');
};

$(document).on('ready', function() {
    $('#confirm-modal .btn-confirm').bind('click', function() {
        window.confirmDialog.acceptCallback();
    });
    window.app = {};
    window.app.core = new Core();
    window.app.historyManager = new HistoryManager(window.app.core);
    window.app.levelImporter = new LevelImporter(window.app.core);
    window.app.levelExporter = new LevelExporter(window.app.core);
    window.app.palette = new Palette(window.app.core, '#palette', '#field-sprite');
    window.app.canvas = new Canvas(window.app.core, '#canvas', '#canvas-mode-control', '#field-sprite');
    window.app.layerManager = new LayerManager(window.app.core, '#layer-manager');
    window.app.fileSelector = new FileSelector(window.app.levelImporter, '#btn-open', '#file-open-modal');
    window.app.fileSaver = new FileSaver(window.app.levelExporter, '#btn-save', '#input-filename');
});
