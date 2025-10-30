# templates/_helpers.tpl
{{/* Generate a name using the release name */}}
{{- define "notification-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "notification-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

