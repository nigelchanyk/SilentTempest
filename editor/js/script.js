String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

// Class Core
Core = function(canvas) {
    this.canvas = canvas;
    this.subscribers = {
        onCellChanged: Array(),
        onLayerChanged: Array(),
        onLayerCountChanged: Array(),
        onLayerGroupChanged: Array(),
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

Core.prototype.bind = function(evt, callback) {
    this.subscribers[evt].push(callback);
}

Core.prototype.getLevel = function(level) {
    return this.level;
};

Core.prototype.notify = function(evt, change) {
    var subscribers = this.subscribers[evt];
    for (var i = 0; i < subscribers.length; ++i)
        subscribers[i](change);
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

Core.prototype.setLayer = function(layer) {
    this.layer = layer;
    this.notify('onLayerChanged', layer);
};

Core.prototype.setLayerGroup = function(layerGroup) {
    this.setLayer(0);
    this.layerGroup = layerGroup;
    this.notify('onLayerGroupChanged', layerGroup);
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
    var maxX = $(this.palette).width() - 1;
    var maxY = $(this.palette).height() - 1;
    var startX = ~~(Math.min(this.startX, maxX) / 32);
    var startY = ~~(Math.min(this.startY, maxY) / 32);
    var endX = ~~(Math.min(this.endX, maxX) / 32);
    var endY = ~~(Math.min(this.endY, maxY) / 32);
    this.core.setPaletteArea(
        new Area(
            Math.min(startY, endY),
            Math.min(startX, endX),
            Math.abs(endX - startX) + 1,
            Math.abs(endY - startY) + 1
        )
    );
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
Canvas = function(core, canvas, fieldSprite) {
    this.core = core;
    this.canvas = canvas;
    this.fieldSprite = $(fieldSprite)[0];
    this.ctx = $(canvas)[0].getContext('2d');
    var _this = this;
    core.bind('onLevelReset', function(level) {
        _this.repaint(level);
    });
    this.repaint(core.getLevel());
};

Canvas.prototype.repaint = function(level) {
    $(this.canvas).attr({
        width: level.getColumnCount() * 32,
        height: level.getRowCount() * 32
    });
    var rows = this.core.getLevel().getRowCount();
    var cols = this.core.getLevel().getColumnCount();
    for (var r = 0; r < rows; ++r) {
        for (var c = 0; c < cols; ++c)
            this.paintCell(r, c);
    }
};

Canvas.prototype.paintCell = function(row, column) {
    var groundDepth = this.core.getLevel().getGroundDepthCount();
    for (var i = 0; i < groundDepth; ++i) {
        var index = this.core.getLevel().getGroundGrid(i, row, column);
        this.ctx.drawImage(this.fieldSprite, (index % 5) * 32, (~~(index / 5)) * 32, 32, 32, column * 32, row * 32, 32, 32);
    }
    var topDepth = this.core.getLevel().getTopDepthCount();
    for (var i = 0; i < topDepth; ++i) {
        var index = this.core.getLevel().getTopGrid(i, row, column);
        this.ctx.drawImage(this.fieldSprite, (index % 5) * 32, (~~(index / 5)) * 32, 32, 32, column * 32, row * 32, 32, 32);
    }
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
            layer[j][k] = 0;
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

Level.prototype.getTopGrid = function(depth, row, column) {
    return this.topLayers[depth][row][column];
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

Level.fromJSON = function(json) {
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

LevelImporter.prototype.load = function(file, errorCallback) {
    var reader = new window.FileReader();
    var _this = this;
    reader.onload = function(evt) {
        try {
            var level = Level.fromJSON($.parseJSON(evt.target.result));
            _this.core.setLevel(level);
        } catch (e) {
            switch (e.name) {
                case 'SyntaxError':
                    errorCallback(LevelImporter.Errors.SyntaxError);
                    break;
                default:
                    errorCallback(LevelImporter.Errors.GenericError);

            }
        }
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
        return _this.onConfirm(e);
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
    $(dialog).modal('close');
    var _this = this;
    this.loader.load(this.file, function(message) {
        window.showMessage(message, 'Error');
    });
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
    var core = new Core();
    var levelImporter = new LevelImporter(core);
    var palette = new Palette(core, '#palette', '#field-sprite');
    var canvas = new Canvas(core, '#canvas', '#field-sprite');
    var layerManager = new LayerManager(core, '#layer-manager');
    var fileSelector = new FileSelector(levelImporter, '#btn-open', '#file-open-modal');
});
