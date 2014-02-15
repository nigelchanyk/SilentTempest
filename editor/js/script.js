String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

// Class Core
Core = function(canvas) {
    this.canvas = canvas;
    this.subscribers = {
        onCellChanged: Array(),
        onLevelReset: Array(),
        onPaletteAreaChanged: Array()
    };
    this.setLevel(new Level(1, 1, 15, 20));
    this.setPaletteArea(new Area(0, 0, 1, 1));
};

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
    this.notify('onLevelReset', level);
};

Core.prototype.getPaletteArea = function() {
    return this.paletteArea;
};

Core.prototype.setPaletteArea = function(area) {
    this.paletteArea = area;
    this.notify('onPaletteAreaChanged', area);
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
    $(palette).bind('mouseup mouseexit', function(evt) {
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
    this.startX = evt.clientX - viewportOffset.left;
    this.startY = evt.clientY - viewportOffset.top;
    this.dragging = true;
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
    var startX = ~~(this.startX / 32);
    var startY = ~~(this.startY / 32);
    var endX = ~~(this.endX / 32);
    var endY = ~~(this.endY / 32);
    this.core.setPaletteArea(
        new Area(
            Math.min(startY, endY),
            Math.min(startX, endX),
            Math.abs(endX - startX) + 1,
            Math.abs(endY - startY) + 1
        )
    );
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
    for (var i = 0; i < depth; ++i) {
        layers[i] = new Array(row);
        for (var j = 0; j < row; ++j) {
            layers[i][j] = new Array(column);
            for (var k = 0; k < column; ++k) {
                layers[i][j][k] = 0;
            }
        }
    }
    return layers;
}

Level.prototype.getRowCount = function() {
    return this.row;
}

Level.prototype.getColumnCount = function() {
    return this.column;
}

Level.prototype.getGroundDepthCount = function() {
    return this.groundDepth;
}

Level.prototype.getTopDepthCount = function() {
    return this.getTopDepthCount;
}

Level.prototype.getGroundGrid = function(depth, row, column) {
    return this.groundLayers[depth][row][column];
}

Level.prototype.getTopGrid = function(depth, row, column) {
    return this.topLayers[depth][row][column];
}

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
}

$(document).on('ready', function() {
    var core = new Core();
    var levelImporter = new LevelImporter(core);
    var palette = new Palette(core, '#palette', '#field-sprite');
    var canvas = new Canvas(core, '#canvas', '#field-sprite');
    var fileSelector = new FileSelector(levelImporter, '#btn-open', '#file-open-modal');
});
