# templates/_helpers.tpl
{{/* Generate a name using the release name */}}
{{- define "cloud-gateway.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "cloud-gateway.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

